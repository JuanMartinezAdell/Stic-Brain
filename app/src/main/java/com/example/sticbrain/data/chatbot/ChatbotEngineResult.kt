package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.model.ChatbotIncidentResult

/**
 * Resultado generado por un motor del chatbot (Local o Gemini).
 */
data class ChatbotEngineResult(
    val answer: String,
    val relatedIncidents: List<ChatbotIncidentResult> = emptyList(),
    val usedExternalAi: Boolean = false
)
