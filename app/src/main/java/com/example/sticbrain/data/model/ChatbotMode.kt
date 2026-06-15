package com.example.sticbrain.data.model

/**
 * Define los modos de funcionamiento del chatbot.
 *
 * LOCAL:
 * El chatbot busca respuestas únicamente en la base de conocimiento local (Room).
 *
 * GEMINI:
 * El chatbot usa una API externa de Gemini configurada por el usuario para generar respuestas.
 */
enum class ChatbotMode {
    LOCAL,
    GEMINI
}
