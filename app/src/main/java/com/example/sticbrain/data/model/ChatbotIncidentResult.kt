package com.example.sticbrain.data.model

/**
 * Modelo que representa un resultado de búsqueda del chatbot.
 * 
 * Contiene la información necesaria para mostrar una tarjeta resumen
 * de una ficha de conocimiento encontrada por el asistente.
 */
data class ChatbotIncidentResult(
    val incidenciaId: Long,
    val tituloNombre: String,
    val categoria: String,
    val nivelPrioridad: String,
    val procedimientoResumen: String,
    val puntuacion: Int,
    val motivoCoincidencia: String? = null
)
