package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.local.entity.IncidenciaEntity

/**
 * Interfaz común para cualquier motor de respuesta del chatbot.
 *
 * Esto permite tener un motor local y, en el futuro, un motor Gemini
 * sin cambiar la estructura principal de la pantalla del chatbot.
 */
interface ChatbotEngine {

    /**
     * Genera una respuesta basada en la pregunta del usuario y el contexto local.
     */
    suspend fun generateResponse(
        question: String,
        incidencias: List<IncidenciaEntity>
    ): ChatbotEngineResult
}
