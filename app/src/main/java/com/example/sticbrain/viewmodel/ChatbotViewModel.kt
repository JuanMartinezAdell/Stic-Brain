package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.chatbot.ChatbotEngine
import com.example.sticbrain.data.chatbot.ChatbotGenerationOptions
import com.example.sticbrain.data.chatbot.GeminiChatbotEngine
import com.example.sticbrain.data.chatbot.LocalChatbotEngine
import com.example.sticbrain.data.local.entity.ChatMessageEntity
import com.example.sticbrain.data.mapper.toChatMessage
import com.example.sticbrain.data.model.ChatbotDetailLevel
import com.example.sticbrain.data.model.ChatbotMode
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
        }
    }
}
