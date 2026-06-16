package com.example.sticbrain.data.model

/**
 * Define cuánto detalle debe incluir la respuesta del chatbot.
 */
enum class ChatbotDetailLevel {
    /** Respuesta rápida y directa. */
    BAJO,
    
    /** Respuesta equilibrada con explicación y pasos principales. */
    MEDIO,
    
    /** Respuesta más completa, con motivo, pasos, alternativas y comprobaciones. */
    ALTO
}
