package com.example.sticbrain.data.model

/**
 * Modelo de datos para representar un mensaje en la conversación del chatbot.
 *
 * @property id Identificador único para el mensaje (basado en tiempo).
 * @property text Contenido del mensaje.
 * @property isUser Indica si el mensaje es del usuario (true) o del asistente (false).
 * @property relatedIncidents Lista de fichas relacionadas encontradas (solo para mensajes del bot).
 */
data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isUser: Boolean,
    val relatedIncidents: List<ChatbotIncidentResult> = emptyList()
)
