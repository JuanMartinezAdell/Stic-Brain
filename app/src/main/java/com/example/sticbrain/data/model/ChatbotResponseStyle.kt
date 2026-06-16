package com.example.sticbrain.data.model

/**
 * Define el estilo de respuesta que debe generar el chatbot.
 */
enum class ChatbotResponseStyle {
    /** Respuesta corta centrada en causa probable y solución principal. */
    DIAGNOSTICO_BREVE,
    
    /** Respuesta ordenada con pasos claros para resolver la incidencia. */
    PROCEDIMIENTO_PASO_A_PASO,
    
    /** Respuesta más general, útil para responsables o supervisores. */
    RESUMEN_EJECUTIVO
}
