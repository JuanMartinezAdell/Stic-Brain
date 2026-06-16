package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.model.ChatbotIncidentResult

/**
 * Resultado avanzado generado por un motor del chatbot.
 * 
 * Además de la respuesta textual, incluye información estructurada sobre
 * la relevancia de las fichas y el nivel de seguridad de la IA.
 */
data class ChatbotEngineResult(
    val answer: String,
    val relatedIncidents: List<ChatbotIncidentResult> = emptyList(),
    val usedExternalAi: Boolean = false,
    val confidence: String? = null,
    val mainIncidentId: Long? = null,
    val alternativeIncidentIds: List<Long> = emptyList()
)
