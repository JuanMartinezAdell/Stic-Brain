package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que almacena la configuración del chatbot.
 *
 * Incluye el modo de funcionamiento, la API key de Gemini y la información
 * de la cuenta Google confirmada por el usuario.
 */
@Entity(tableName = "chatbot_config")
data class ChatbotConfigEntity(
    @PrimaryKey
    val id: Int = 1,

    val mode: String = "LOCAL",

    val geminiApiKey: String? = null,

    val geminiModel: String = "gemini-1.5-flash",

    // Campos para la confirmación con cuenta Google
    val googleEmail: String? = null,
    val googleDisplayName: String? = null,
    val googleAccountConfirmed: Boolean = false,

    val lastUpdated: Long = System.currentTimeMillis()
)
