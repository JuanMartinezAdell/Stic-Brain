package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa un mensaje guardado del chatbot.
 */
@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val relatedIncidentIds: String? = null,
    
    // Nuevos campos para trazabilidad y UI mejorada
    val usedExternalAi: Boolean = false,
    val confidence: String? = null,
    val mainIncidentId: Long? = null
)
