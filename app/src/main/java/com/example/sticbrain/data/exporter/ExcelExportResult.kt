package com.example.sticbrain.data.exporter

/**
 * Representa el resumen de una operación de exportación a Excel.
 */
data class ExcelExportResult(
    val totalFichas: Int,
    val exportadas: Int,
    val fileName: String? = null,
    val errorMessage: String? = null
)
