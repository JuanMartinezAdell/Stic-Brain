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
    val mainIncidentId: Long? = null
)
