package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.chatbot.ChatbotEngine
import com.example.sticbrain.data.chatbot.ChatbotGenerationOptions
import com.example.sticbrain.data.chatbot.GeminiApiClient
import com.example.sticbrain.data.chatbot.GeminiChatbotEngine
import com.example.sticbrain.data.chatbot.GeminiExternalSearchEngine
import com.example.sticbrain.data.chatbot.LocalChatbotEngine
import com.example.sticbrain.data.local.entity.ChatMessageEntity
import com.example.sticbrain.data.mapper.toChatMessage
import com.example.sticbrain.data.model.ChatbotDetailLevel
import com.example.sticbrain.data.model.ChatbotMode
import com.example.sticbrain.data.model.ChatbotPendingAction
import com.example.sticbrain.data.model.ChatbotPendingActionType
import com.example.sticbrain.data.model.ChatbotResponseStyle
import com.example.sticbrain.data.model.ChatMessage
import com.example.sticbrain.data.model.GeneratedIncidentDraft
import com.example.sticbrain.data.repository.ChatbotConfigRepository
import com.example.sticbrain.data.repository.ChatMessageRepository
import com.example.sticbrain.data.repository.IncidenciaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel del Chatbot de Stic Brain.
 * 
 * Ahora gestiona respuestas estructuradas, niveles de confianza y distinción
 * entre ficha principal y alternativas.
 */
class ChatbotViewModel(
    private val incidenciaRepository: IncidenciaRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatbotConfigRepository: ChatbotConfigRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _currentQuestion = MutableStateFlow("")
    val currentQuestion: StateFlow<String> = _currentQuestion.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentModeLabel = MutableStateFlow("Modo local")
    val currentModeLabel: StateFlow<String> = _currentModeLabel.asStateFlow()

    // Acción pendiente que requiere confirmación del usuario
    private val _pendingAction = MutableStateFlow<ChatbotPendingAction?>(null)
    val pendingAction: StateFlow<ChatbotPendingAction?> = _pendingAction.asStateFlow()

    init {
        cargarHistorial()
        observarConfiguracion()
    }

    private fun observarConfiguracion() {
        viewModelScope.launch {
            chatbotConfigRepository.obtenerConfiguracion().collect { config ->
                _currentModeLabel.value = if (config?.mode == ChatbotMode.GEMINI.name) "Modo Gemini" else "Modo local"
            }
        }
    }

    private fun cargarHistorial() {
        viewModelScope.launch {
            chatMessageRepository.obtenerHistorial().collectLatest { entidades ->
                _messages.value = reconstruirMensajes(entidades)
            }
        }
    }

    private suspend fun reconstruirMensajes(entidades: List<ChatMessageEntity>): List<ChatMessage> {
        return withContext(Dispatchers.IO) {
            entidades.map { entidad ->
                val mensajeUi = entidad.toChatMessage()
                if (!entidad.isUser && !entidad.relatedIncidentIds.isNullOrBlank()) {
                    val ids = entidad.relatedIncidentIds.split(",").mapNotNull { it.toLongOrNull() }
                    val fichas = incidenciaRepository.obtenerIncidenciasPorIds(ids)
                    
                    // Mapeamos las fichas recuperadas a resultados de UI
                    val resultados = fichas.map { ficha ->
                        com.example.sticbrain.data.model.ChatbotIncidentResult(
                            incidenciaId = ficha.id,
                            tituloNombre = ficha.tituloNombre,
                            categoria = ficha.categoria,
                            nivelPrioridad = ficha.nivelPrioridad,
                            procedimientoResumen = if (ficha.procedimientoRespuesta.length > 150) ficha.procedimientoRespuesta.take(150) + "..." else ficha.procedimientoRespuesta,
                            puntuacion = 0 // La puntuación no se persiste
                        )
                    }
                    mensajeUi.copy(relatedIncidents = resultados)
                } else {
                    mensajeUi
                }
            }
        }
    }

    fun onQuestionChange(value: String) {
        _currentQuestion.value = value
    }

    fun sendQuestion() {
        val question = _currentQuestion.value.trim()
        if (question.isBlank()) return

        _currentQuestion.value = ""
        _isLoading.value = true

        viewModelScope.launch {
            // Guardar pregunta del usuario
            chatMessageRepository.insertarMensaje(ChatMessageEntity(text = question, isUser = true))

            val config = withContext(Dispatchers.IO) {
                chatbotConfigRepository.obtenerConfiguracionUnaVez()
            }
            
            val engine: ChatbotEngine = if (config?.mode == ChatbotMode.GEMINI.name) {
                val apiKey = withContext(Dispatchers.IO) { chatbotConfigRepository.obtenerGeminiApiKey() }
                
                if (apiKey.isNullOrBlank()) {
                    chatMessageRepository.insertarMensaje(
                        ChatMessageEntity(
                            text = "Has seleccionado el modo Gemini, pero no hay una API key configurada. Ve a Ajustes > Configuración del chatbot.",
                            isUser = false
                        )
                    )
                    _isLoading.value = false
                    return@launch
                }
                
                if (!config.googleAccountConfirmed) {
                    chatMessageRepository.insertarMensaje(
                        ChatMessageEntity(
                            text = "Para usar Gemini debes confirmar primero tu cuenta de Google en Ajustes > Configuración del chatbot.",
                            isUser = false
                        )
                    )
                    _isLoading.value = false
                    return@launch
                }

                GeminiChatbotEngine(
                    apiKey = apiKey, 
                    model = config.geminiModel
                )
            } else {
                LocalChatbotEngine()
            }

            val options = if (config != null) {
                ChatbotGenerationOptions(
                    maxContextIncidents = config.maxContextIncidents,
                    responseStyle = ChatbotResponseStyle.valueOf(config.responseStyle),
                    detailLevel = ChatbotDetailLevel.valueOf(config.detailLevel)
                )
            } else {
                ChatbotGenerationOptions()
            }

            val incidencias = withContext(Dispatchers.IO) { incidenciaRepository.obtenerIncidenciasUnaVez() }
            
            try {
                val engineResult = engine.generateResponse(question, incidencias, options)

                // Si no hay información suficiente, preparamos la acción pendiente
                if (engineResult.needsExternalSearchConfirmation) {
                    val pending = ChatbotPendingAction(
                        type = ChatbotPendingActionType.CONFIRM_EXTERNAL_SEARCH,
                        originalQuestion = question,
                        localResultSummary = engineResult.answer
                    )
                    _pendingAction.value = pending

                    val idsRelacionados = engineResult.relatedIncidents.map { it.incidenciaId }.joinToString(",").ifBlank { null }
                    
                    // Persistimos el mensaje. El flag requiresUserConfirmation se manejará en la UI
                    // comparando con _pendingAction.
                    chatMessageRepository.insertarMensaje(
                        ChatMessageEntity(
                            text = engineResult.answer,
                            isUser = false,
                            relatedIncidentIds = idsRelacionados,
                            confidence = engineResult.confidence
                        )
                    )
                    
                    _isLoading.value = false
                    return@launch
                }

                val idsRelacionados = engineResult.relatedIncidents.map { it.incidenciaId }.joinToString(",").ifBlank { null }
                
                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = engineResult.answer,
                        isUser = false,
                        relatedIncidentIds = idsRelacionados,
                        usedExternalAi = engineResult.usedExternalAi,
                        confidence = engineResult.confidence,
                        mainIncidentId = engineResult.mainIncidentId
                    )
                )
            } catch (e: Exception) {
                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = "Error al procesar la respuesta: ${e.localizedMessage}",
                        isUser = false
                    )
                )
            }

            _isLoading.value = false
        }
    }

    fun clearConversation() {
        viewModelScope.launch {
            chatMessageRepository.limpiarHistorial()
            _currentQuestion.value = ""
            _pendingAction.value = null
        }
    }

    private fun crearOpcionesDesdeConfiguracion(config: com.example.sticbrain.data.local.entity.ChatbotConfigEntity?): ChatbotGenerationOptions {
        return if (config != null) {
            ChatbotGenerationOptions(
                maxContextIncidents = config.maxContextIncidents,
                responseStyle = runCatching { ChatbotResponseStyle.valueOf(config.responseStyle) }
                    .getOrDefault(ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO),
                detailLevel = runCatching { ChatbotDetailLevel.valueOf(config.detailLevel) }
                    .getOrDefault(ChatbotDetailLevel.MEDIO)
            )
        } else {
            ChatbotGenerationOptions()
        }
    }

    /**
     * Confirma la búsqueda de información externa mediante Gemini.
     */
    fun confirmExternalSearch() {
        val action = _pendingAction.value ?: return
        if (action.type != ChatbotPendingActionType.CONFIRM_EXTERNAL_SEARCH) return

        val originalQuestion = action.originalQuestion
        _pendingAction.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Notificar la decisión en el chat
                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = "Has autorizado la búsqueda de información externa mediante Gemini.",
                        isUser = true
                    )
                )

                val config = withContext(Dispatchers.IO) { chatbotConfigRepository.obtenerConfiguracionUnaVez() }
                val apiKey = withContext(Dispatchers.IO) { chatbotConfigRepository.obtenerGeminiApiKey() }

                if (apiKey.isNullOrBlank()) {
                    chatMessageRepository.insertarMensaje(
                        ChatMessageEntity(
                            text = "No puedo buscar información externa porque no hay una API key de Gemini configurada.\n\nVe a Ajustes > Configuración del chatbot e introduce tu API key.",
                            isUser = false
                        )
                    )
                    return@launch
                }

                val apiClient = GeminiApiClient(apiKey, config?.geminiModel ?: "gemini-1.5-flash")
                val externalEngine = GeminiExternalSearchEngine(apiClient)
                val options = crearOpcionesDesdeConfiguracion(config)

                val result = externalEngine.generateExternalSolution(originalQuestion, options)

                val draft = crearBorradorDesdeRespuestaExterna(
                    preguntaOriginal = originalQuestion,
                    respuestaGemini = result.answer
                )

                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = result.answer,
                        isUser = false,
                        usedExternalAi = true,
                        confidence = result.confidence
                    )
                )
                
                // NOTA: Para que el botón aparezca en la UI de la sesión actual, 
                // ya que los mensajes se recargan de Room y ChatMessageEntity no guarda el draft,
                // podríamos necesitar una forma de indicar que el último mensaje es de Gemini externo.
                // En este flujo, el mensaje persistido NO tiene el draft, pero la UI lo detectará
                // si el mensaje tiene usedExternalAi = true.
                
            } catch (e: Exception) {
                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = "Se ha producido un error al buscar información externa con Gemini: ${e.localizedMessage}",
                        isUser = false
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Crea un borrador de ficha técnica a partir de una respuesta externa.
     */
    private fun crearBorradorDesdeRespuestaExterna(
        preguntaOriginal: String,
        respuestaGemini: String
    ): GeneratedIncidentDraft {
        val tituloSugerido = preguntaOriginal
            .take(60)
            .replaceFirstChar { it.uppercase() }

        val palabrasClave = preguntaOriginal
            .lowercase()
            .replace(Regex("[^a-záéíóúñ0-9\\s]"), " ")
            .split(" ")
            .filter { it.length >= 4 }
            .distinct()
            .take(6)
            .joinToString(", ")

        return GeneratedIncidentDraft(
            originalQuestion = preguntaOriginal,
            generatedAnswer = respuestaGemini,
            suggestedTitle = tituloSugerido,
            suggestedKeywords = palabrasClave
        )
    }

    /**
     * Cancela la búsqueda externa y mantiene el modo local.
     */
    fun cancelExternalSearch() {
        _pendingAction.value = null
        viewModelScope.launch {
            chatMessageRepository.insertarMensaje(
                ChatMessageEntity(
                    text = "Has decidido mantener la consulta solo con información local.",
                    isUser = true
                )
            )
            
            chatMessageRepository.insertarMensaje(
                ChatMessageEntity(
                    text = "De acuerdo. Mantendré la consulta dentro de la base de conocimiento local.\n\nPuedes probar con otras palabras clave o crear una nueva ficha para documentar esta incidencia.",
                    isUser = false
                )
            )
        }
    }
}
