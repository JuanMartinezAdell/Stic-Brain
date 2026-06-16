package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.model.ChatbotIncidentResult

/**
 * Resultado avanzado generado por un motor del chatbot.
 */
data class ChatbotEngineResult(
    val answer: String,
    val relatedIncidents: List<ChatbotIncidentResult> = emptyList(),
    val usedExternalAi: Boolean = false,
    val confidence: String? = null,
    val mainIncidentId: Long? = null,
    val alternativeIncidentIds: List<Long> = emptyList(),
    
    // Nuevos campos para control de flujo
    val hasEnoughLocalInformation: Boolean = true,
    val needsExternalSearchConfirmation: Boolean = false
)
