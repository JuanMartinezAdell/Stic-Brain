package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa un mensaje guardado del chatbot.
 *
 * Cada registro de esta tabla corresponde a un mensaje de la conversación,
 * ya sea escrito por el usuario o generado por el chatbot.
 *
 * @property id Identificador autogenerado del mensaje.
 * @property text Contenido textual del mensaje.
 * @property isUser Indica si el mensaje fue escrito por el usuario (true) o el bot (false).
 * @property timestamp Momento exacto de la creación del mensaje.
 * @property relatedIncidentIds IDs de fichas relacionadas guardados como texto separado por comas (ej: "1,4,7").
 */
@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val relatedIncidentIds: String? = null
)
