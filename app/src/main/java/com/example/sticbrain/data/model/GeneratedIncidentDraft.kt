package com.example.sticbrain.data.model

/**
 * Modelo temporal que representa una ficha propuesta a partir
 * de una respuesta externa generada por Gemini.
 *
 * No se guarda directamente hasta que el usuario la revise y confirme en la pantalla de edición.
 */
data class GeneratedIncidentDraft(
    val originalQuestion: String,
    val generatedAnswer: String,
    val suggestedTitle: String,
    val suggestedCategory: String = "Pendiente de clasificar",
    val suggestedKeywords: String = "",
    val suggestedPriority: String = "Normal"
)
