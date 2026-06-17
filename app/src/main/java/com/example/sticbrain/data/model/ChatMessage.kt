package com.example.sticbrain.data.model

/**
 * Modelo de datos para representar un mensaje en la conversación del chatbot.
 */
data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isUser: Boolean,
    val relatedIncidents: List<ChatbotIncidentResult> = emptyList(),
    val usedExternalAi: Boolean = false,
    val confidence: String? = null,
    val mainIncidentId: Long? = null,
    
    // Nuevos campos para acciones pendientes
    val requiresUserConfirmation: Boolean = false,
    val pendingAction: ChatbotPendingAction? = null,

    /**
     * Indica si desde este mensaje se puede crear una ficha provisional.
     */
    val canCreateDraftIncident: Boolean = false,

    /**
     * Datos sugeridos para crear una ficha provisional.
     */
    val generatedIncidentDraft: GeneratedIncidentDraft? = null
)
