package com.example.sticbrain.data.importer

/**
 * Representa el resumen de una operación de importación.
 */
data class ExcelImportResult(
    val totalFilas: Int,
    val filasValidas: Int,
    val filasInvalidas: Int,
    val errores: List<String>
)
