package com.example.sticbrain.data.importer

/**
 * Representa una fila individual de conocimiento extraída de la plantilla Excel.
 */
data class ExcelKnowledgeRow(
    val idOriginal: String? = null,
    val categoria: String,
    val tituloNombre: String,
    val descripcion: String,
    val frasesUsuario: String,
    val procedimientoRespuesta: String,
    val palabrasClave: String,
    val nivelPrioridad: String,
    val notasComentarios: String? = null
)
