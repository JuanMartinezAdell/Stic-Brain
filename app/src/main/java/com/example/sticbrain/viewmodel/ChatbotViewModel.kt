package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.chatbot.ChatbotEngine
import com.example.sticbrain.data.chatbot.GeminiChatbotEngine
import com.example.sticbrain.data.chatbot.LocalChatbotEngine
import com.example.sticbrain.data.local.entity.ChatMessageEntity
import com.example.sticbrain.data.mapper.toChatMessage
import com.example.sticbrain.data.model.ChatbotMode
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
 * Ahora decide qué motor de IA usar (Local o Gemini) basándose en la configuración.
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
    /** Etiqueta que indica el modo actual para mostrar en la UI. */
    val currentModeLabel: StateFlow<String> = _currentModeLabel.asStateFlow()

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
                    val resultados = fichas.map { it.toChatbotIncidentResult(0) }
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

    /**
     * Envía la pregunta seleccionando el motor adecuado según la configuración.
     */
    fun sendQuestion() {
        val question = _currentQuestion.value.trim()
        if (question.isBlank()) return

        _currentQuestion.value = ""
        _isLoading.value = true

        viewModelScope.launch {
            // 1. Guardar mensaje del usuario
            chatMessageRepository.insertarMensaje(ChatMessageEntity(text = question, isUser = true))

            // 2. Obtener configuración y seleccionar motor
            val config = withContext(Dispatchers.IO) {
                chatbotConfigRepository.obtenerConfiguracionUnaVez()
            }
            
            val engine: ChatbotEngine = if (config?.mode == ChatbotMode.GEMINI.name) {
                if (config.geminiApiKey.isNullOrBlank()) {
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

                GeminiChatbotEngine(apiKey = config.geminiApiKey, model = config.geminiModel)
            } else {
                LocalChatbotEngine()
            }

            // 3. Generar respuesta
            val incidencias = withContext(Dispatchers.IO) { incidenciaRepository.obtenerIncidenciasUnaVez() }
            val engineResult = engine.generateResponse(question, incidencias)

            // 4. Guardar respuesta del bot
            val idsRelacionados = engineResult.relatedIncidents.map { it.incidenciaId }.joinToString(",").ifBlank { null }
            val finalAnswer = if (engineResult.usedExternalAi) {
                "${engineResult.answer}\n\n[Respuesta generada con Gemini usando el contexto de Stic Brain]"
            } else {
                "${engineResult.answer}\n\n[Respuesta generada en modo local usando Room]"
            }

            chatMessageRepository.insertarMensaje(
                ChatMessageEntity(
                    text = finalAnswer,
                    isUser = false,
                    relatedIncidentIds = idsRelacionados
                )
            )

            _isLoading.value = false
        }
    }

    fun clearConversation() {
        viewModelScope.launch {
            chatMessageRepository.limpiarHistorial()
            _currentQuestion.value = ""
        }
    }

    // Función auxiliar para el mapeo que se usaba antes
    private fun com.example.sticbrain.data.local.entity.IncidenciaEntity.toChatbotIncidentResult(puntuacion: Int) =
        com.example.sticbrain.data.model.ChatbotIncidentResult(
            incidenciaId = id,
            tituloNombre = tituloNombre,
            categoria = categoria,
            nivelPrioridad = nivelPrioridad,
            procedimientoResumen = if (procedimientoRespuesta.length > 150) procedimientoRespuesta.take(150) + "..." else procedimientoRespuesta,
            puntuacion = puntuacion
        )
}
