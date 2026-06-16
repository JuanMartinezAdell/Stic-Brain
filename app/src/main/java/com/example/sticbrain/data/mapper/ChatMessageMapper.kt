package com.example.sticbrain.data.mapper

import com.example.sticbrain.data.local.entity.ChatMessageEntity
import com.example.sticbrain.data.model.ChatMessage

/**
 * Convierte una entidad de Room en un mensaje de la interfaz.
 */
fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        text = text,
        isUser = isUser,
        relatedIncidents = emptyList(),
        usedExternalAi = usedExternalAi,
        confidence = confidence,
        mainIncidentId = mainIncidentId
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
        id = 0,
        text = text,
        isUser = isUser,
        relatedIncidentIds = idsRelacionados,
        usedExternalAi = usedExternalAi,
        confidence = confidence,
        mainIncidentId = mainIncidentId
    )
}
