package com.example.sticbrain.data.model

/**
 * Estado que utiliza la pantalla de configuración del chatbot.
 */
data class ChatbotConfigUiState(
    val mode: ChatbotMode = ChatbotMode.LOCAL,
    val geminiApiKeyInput: String = "",
    val hasGeminiApiKey: Boolean = false,
    val geminiModel: String = "gemini-1.5-flash",
    
    // Opciones avanzadas
    val maxContextIncidents: Int = 5,
    val responseStyle: ChatbotResponseStyle = ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO,
    val detailLevel: ChatbotDetailLevel = ChatbotDetailLevel.MEDIO,

    val googleEmail: String? = null,
    val googleDisplayName: String? = null,
    val googleAccountConfirmed: Boolean = false,
    val isGoogleSigningIn: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
