package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.local.entity.IncidenciaEntity

/**
 * Interfaz común para cualquier motor de respuesta del chatbot.
 */
interface ChatbotEngine {

    /**
     * Genera una respuesta basada en la pregunta del usuario, el contexto local
     * y las opciones de generación configuradas.
     */
    suspend fun generateResponse(
        question: String,
        incidencias: List<IncidenciaEntity>,
        options: ChatbotGenerationOptions = ChatbotGenerationOptions()
    ): ChatbotEngineResult
}
