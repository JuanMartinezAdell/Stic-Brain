package com.example.sticbrain.data.mapper

import com.example.sticbrain.data.local.entity.ChatMessageEntity
import com.example.sticbrain.data.model.ChatMessage

/**
 * Convierte una entidad de Room en un mensaje que puede mostrar la interfaz.
 *
 * El campo relatedIncidents se deja vacío inicialmente; el ViewModel se
 * encargará de reconstruirlo si existen IDs relacionados.
 */
fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        text = text,
        isUser = isUser,
        relatedIncidents = emptyList()
    )
}

/**
 * Convierte un mensaje de la interfaz en una entidad Room.
 */
fun ChatMessage.toEntity(): ChatMessageEntity {
    val idsRelacionados = relatedIncidents
        .map { it.incidenciaId }
        .joinToString(",")
        .ifBlank { null }

    return ChatMessageEntity(
        id = 0, // Room generará el ID
        text = text,
        isUser = isUser,
        relatedIncidentIds = idsRelacionados
    )
}
