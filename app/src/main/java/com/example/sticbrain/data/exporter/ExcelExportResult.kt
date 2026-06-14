package com.example.sticbrain.data.exporter

/**
 * Representa el resumen de la operación de exportación a Excel.
 */
data class ExcelExportResult(
    val totalFichas: Int,
    val exportadas: Int,
    val fileName: String? = null,
    val errorMessage: String? = null
)
