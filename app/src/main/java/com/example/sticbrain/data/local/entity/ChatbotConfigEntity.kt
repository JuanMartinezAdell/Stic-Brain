package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que almacena la configuración del chatbot.
 * 
 * Contiene tanto los ajustes de identidad como las preferencias de generación
 * de respuestas para Gemini y el modo local.
 */
@Entity(tableName = "chatbot_config")
data class ChatbotConfigEntity(
    @PrimaryKey
    val id: Int = 1,

    val mode: String = "LOCAL",

    val geminiModel: String = "gemini-1.5-flash",

    val googleEmail: String? = null,
    val googleDisplayName: String? = null,
    val googleAccountConfirmed: Boolean = false,

    val hasGeminiApiKey: Boolean = false,
    
    // Configuración avanzada de la IA
    val maxContextIncidents: Int = 5,
    val responseStyle: String = "PROCEDIMIENTO_PASO_A_PASO",
    val detailLevel: String = "MEDIO",

    val lastUpdated: Long = System.currentTimeMillis()
)
