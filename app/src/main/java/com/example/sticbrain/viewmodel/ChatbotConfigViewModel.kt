package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.ChatbotConfigEntity
import com.example.sticbrain.data.model.ChatbotConfigUiState
import com.example.sticbrain.data.model.ChatbotDetailLevel
import com.example.sticbrain.data.model.ChatbotMode
import com.example.sticbrain.data.model.ChatbotResponseStyle
import com.example.sticbrain.data.repository.ChatbotConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la configuración del chatbot e identidad de Google.
 * 
 * Ahora gestiona también las opciones avanzadas de generación de respuestas.
 */
class ChatbotConfigViewModel(
    private val chatbotConfigRepository: ChatbotConfigRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatbotConfigUiState())
    val uiState: StateFlow<ChatbotConfigUiState> = _uiState.asStateFlow()

    init {
        cargarConfiguracion()
    }

    private fun cargarConfiguracion() {
        viewModelScope.launch {
            chatbotConfigRepository.obtenerConfiguracion().collect { config ->
                if (config != null) {
                    _uiState.value = _uiState.value.copy(
                        mode = runCatching { ChatbotMode.valueOf(config.mode) }
                            .getOrDefault(ChatbotMode.LOCAL),
                        hasGeminiApiKey = config.hasGeminiApiKey,
                        geminiModel = config.geminiModel,
                        maxContextIncidents = config.maxContextIncidents,
                        responseStyle = runCatching { ChatbotResponseStyle.valueOf(config.responseStyle) }
                            .getOrDefault(ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO),
                        detailLevel = runCatching { ChatbotDetailLevel.valueOf(config.detailLevel) }
                            .getOrDefault(ChatbotDetailLevel.MEDIO),
                        allowExternalSearchWhenNoLocalInfo = config.allowExternalSearchWhenNoLocalInfo,
                        googleEmail = config.googleEmail,
                        googleDisplayName = config.googleDisplayName,
                        googleAccountConfirmed = config.googleAccountConfirmed
                    )
                }
            }
        }
    }

    fun onModeChange(mode: ChatbotMode) {
        _uiState.value = _uiState.value.copy(mode = mode)
    }

    fun onApiKeyChange(value: String) {
        _uiState.value = _uiState.value.copy(geminiApiKeyInput = value)
    }

    fun onModelChange(value: String) {
        _uiState.value = _uiState.value.copy(geminiModel = value)
    }

    fun onMaxContextIncidentsChange(value: Int) {
        _uiState.value = _uiState.value.copy(maxContextIncidents = value)
    }

    fun onResponseStyleChange(value: ChatbotResponseStyle) {
        _uiState.value = _uiState.value.copy(responseStyle = value)
    }

    fun onDetailLevelChange(value: ChatbotDetailLevel) {
        _uiState.value = _uiState.value.copy(detailLevel = value)
    }

    fun onAllowExternalSearchChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(allowExternalSearchWhenNoLocalInfo = value)
    }

    fun onGoogleAccountConfirmed(email: String, displayName: String?) {
        viewModelScope.launch {
            chatbotConfigRepository.guardarCuentaGoogleConfirmada(email, displayName)
            _uiState.value = _uiState.value.copy(message = "Cuenta confirmada correctamente.")
        }
    }

    fun eliminarCuentaGoogle() {
        viewModelScope.launch {
            chatbotConfigRepository.eliminarCuentaGoogleConfirmada()
            _uiState.value = _uiState.value.copy(message = "Cuenta desvinculada.")
        }
    }

    fun guardarConfiguracion() {
        val state = _uiState.value

        if (state.mode == ChatbotMode.GEMINI && state.geminiApiKeyInput.isBlank() && !state.hasGeminiApiKey) {
            _uiState.value = state.copy(error = "Para usar Gemini debes introducir una API key.")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isSaving = true, error = null, message = null)

                if (state.geminiApiKeyInput.isNotBlank()) {
                    chatbotConfigRepository.guardarGeminiApiKey(state.geminiApiKeyInput)
                }

                chatbotConfigRepository.guardarConfiguracion(
                    ChatbotConfigEntity(
                        mode = state.mode.name,
                        geminiModel = state.geminiModel.ifBlank { "gemini-1.5-flash" },
                        googleEmail = state.googleEmail,
                        googleDisplayName = state.googleDisplayName,
                        googleAccountConfirmed = state.googleAccountConfirmed,
                        hasGeminiApiKey = state.hasGeminiApiKey || state.geminiApiKeyInput.isNotBlank(),
                        maxContextIncidents = state.maxContextIncidents,
                        responseStyle = state.responseStyle.name,
                        detailLevel = state.detailLevel.name,
                        allowExternalSearchWhenNoLocalInfo = state.allowExternalSearchWhenNoLocalInfo
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    geminiApiKeyInput = "",
                    message = "Configuración guardada correctamente."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Error al guardar: ${e.message}"
                )
            }
        }
    }

    fun eliminarApiKey() {
        viewModelScope.launch {
            chatbotConfigRepository.eliminarGeminiApiKey()
            _uiState.value = ChatbotConfigUiState(
                mode = ChatbotMode.LOCAL,
                geminiApiKeyInput = "",
                message = "API key eliminada de forma segura."
            )
        }
    }
}
