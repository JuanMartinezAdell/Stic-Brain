package com.example.sticbrain.data.exporter

import com.example.sticbrain.data.local.entity.IncidenciaEntity
import java.io.OutputStream

/**
 * Interfaz para el servicio de exportación de datos a Excel.
 */
interface ExcelExporter {
    /**
     * Exporta una lista de incidencias a un OutputStream en formato Excel (.xlsx).
     */
    suspend fun exportToOutputStream(
        incidencias: List<IncidenciaEntity>,
        outputStream: OutputStream
    ): ExcelExportResult
}
