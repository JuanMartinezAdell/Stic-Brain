package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.ChatbotConfigEntity
import com.example.sticbrain.data.model.ChatbotConfigUiState
import com.example.sticbrain.data.model.ChatbotMode
import com.example.sticbrain.data.repository.ChatbotConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la configuración del chatbot e identidad de Google.
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
                        geminiApiKey = config.geminiApiKey.orEmpty(),
                        geminiModel = config.geminiModel,
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
        _uiState.value = _uiState.value.copy(geminiApiKey = value)
    }

    fun onModelChange(value: String) {
        _uiState.value = _uiState.value.copy(geminiModel = value)
    }

    /**
     * Confirma la cuenta de Google.
     */
    fun onGoogleAccountConfirmed(email: String, displayName: String?) {
        viewModelScope.launch {
            chatbotConfigRepository.guardarCuentaGoogleConfirmada(email, displayName)
            _uiState.value = _uiState.value.copy(
                message = "Cuenta de Google confirmada correctamente."
            )
        }
    }

    /**
     * Elimina la vinculación con Google.
     */
    fun eliminarCuentaGoogle() {
        viewModelScope.launch {
            chatbotConfigRepository.eliminarCuentaGoogleConfirmada()
            _uiState.value = _uiState.value.copy(
                message = "Cuenta de Google desvinculada."
            )
        }
    }

    fun guardarConfiguracion() {
        val state = _uiState.value

        if (state.mode == ChatbotMode.GEMINI && state.geminiApiKey.isBlank()) {
            _uiState.value = state.copy(error = "Para usar Gemini debes introducir una API key.")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isSaving = true, error = null, message = null)

                chatbotConfigRepository.guardarConfiguracion(
                    ChatbotConfigEntity(
                        mode = state.mode.name,
                        geminiApiKey = state.geminiApiKey.ifBlank { null },
                        geminiModel = state.geminiModel.ifBlank { "gemini-1.5-flash" }
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
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
            chatbotConfigRepository.eliminarApiKey()
            _uiState.value = ChatbotConfigUiState(
                mode = ChatbotMode.LOCAL,
                message = "API key eliminada. Se ha vuelto al modo local."
            )
        }
    }
}
