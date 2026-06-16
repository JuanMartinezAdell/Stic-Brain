package com.example.sticbrain.data.model

/**
 * Representa una acción pendiente que requiere confirmación del usuario.
 *
 * En este caso se usa cuando el chatbot no encuentra información local suficiente
 * y necesita preguntar si puede buscar información externa mediante Gemini.
 */
data class ChatbotPendingAction(
    val type: ChatbotPendingActionType,
    val originalQuestion: String,
    val localResultSummary: String? = null
)
