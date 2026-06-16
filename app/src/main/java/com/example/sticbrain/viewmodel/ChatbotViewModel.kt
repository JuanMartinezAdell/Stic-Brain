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
                    
                    // Creamos el mensaje pero con el flag de confirmación (esto es temporal en memoria para la sesión)
                    // Para que aparezca en la UI actual lo añadimos a la lista _messages directamente
                    val botConfirmMessage = ChatMessage(
                        text = engineResult.answer,
                        isUser = false,
                        relatedIncidents = engineResult.relatedIncidents.map {
                            com.example.sticbrain.data.model.ChatbotIncidentResult(
                                incidenciaId = it.incidenciaId,
                                tituloNombre = it.tituloNombre,
                                categoria = it.categoria,
                                nivelPrioridad = it.nivelPrioridad,
                                procedimientoResumen = it.procedimientoResumen,
                                puntuacion = it.puntuacion
                            )
                        },
                        confidence = engineResult.confidence,
                        requiresUserConfirmation = true,
                        pendingAction = pending
                    )
                    
                    // Persistimos el mensaje sin los botones (o podríamos añadir lógica para no persistirlos)
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

    /**
     * Confirma la búsqueda de información externa mediante Gemini.
     */
    fun confirmExternalSearch() {
        val action = _pendingAction.value ?: return
        if (action.type != ChatbotPendingActionType.CONFIRM_EXTERNAL_SEARCH) return

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
                            text = "Para buscar información externa necesitas configurar primero tu API key de Gemini en Ajustes > Configuración del chatbot.",
                            isUser = false
                        )
                    )
                    return@launch
                }

                val apiClient = GeminiApiClient(apiKey, config?.geminiModel ?: "gemini-1.5-flash")
                val externalEngine = GeminiExternalSearchEngine(apiClient)

                val options = if (config != null) {
                    ChatbotGenerationOptions(
                        maxContextIncidents = config.maxContextIncidents,
                        responseStyle = ChatbotResponseStyle.valueOf(config.responseStyle),
                        detailLevel = ChatbotDetailLevel.valueOf(config.detailLevel)
                    )
                } else {
                    ChatbotGenerationOptions()
                }

                val result = externalEngine.generateExternalSolution(action.originalQuestion, options)

                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = result.answer,
                        isUser = false,
                        usedExternalAi = true,
                        confidence = result.confidence
                    )
                )
            } catch (e: Exception) {
                chatMessageRepository.insertarMensaje(
                    ChatMessageEntity(
                        text = "Error al realizar la búsqueda externa: ${e.localizedMessage}",
                        isUser = false
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
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
