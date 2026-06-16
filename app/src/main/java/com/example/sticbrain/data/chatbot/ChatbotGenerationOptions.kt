package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.model.ChatbotDetailLevel
import com.example.sticbrain.data.model.ChatbotResponseStyle

/**
 * Opciones avanzadas que modifican cómo el chatbot genera respuestas.
 *
 * Estas opciones se obtienen desde la configuración guardada por el usuario.
 */
data class ChatbotGenerationOptions(
    val maxContextIncidents: Int = 5,
    val responseStyle: ChatbotResponseStyle = ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO,
    val detailLevel: ChatbotDetailLevel = ChatbotDetailLevel.MEDIO
)
