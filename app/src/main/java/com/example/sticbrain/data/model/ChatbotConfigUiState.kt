package com.example.sticbrain.data.model

/**
 * Estado que utiliza la pantalla de configuración del chatbot.
 */
data class ChatbotConfigUiState(
    val mode: ChatbotMode = ChatbotMode.LOCAL,
    val geminiApiKey: String = "",
    val geminiModel: String = "gemini-1.5-flash",
    val googleEmail: String? = null,
    val googleDisplayName: String? = null,
    val googleAccountConfirmed: Boolean = false,
    val isGoogleSigningIn: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
