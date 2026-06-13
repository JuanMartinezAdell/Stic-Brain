package com.example.sticbrain.data.importer

/**
 * Validador para asegurar que los datos obligatorios del Excel están presentes.
 */
object ExcelKnowledgeValidator {

    fun validateRow(row: ExcelKnowledgeRow, rowIndex: Int): String? {
        val missingFields = mutableListOf<String>()
        
        if (row.categoria.isBlank()) missingFields.add("Categoría")
        if (row.tituloNombre.isBlank()) missingFields.add("Título / Nombre")
        if (row.descripcion.isBlank()) missingFields.add("Descripción")
        if (row.procedimientoRespuesta.isBlank()) missingFields.add("Procedimiento / Respuesta")
        if (row.nivelPrioridad.isBlank()) missingFields.add("Nivel de prioridad")
        
        return if (missingFields.isNotEmpty()) {
            "Fila $rowIndex: falta campo(s) ${missingFields.joinToString(", ")}"
        } else {
            null
        }
    }
}
