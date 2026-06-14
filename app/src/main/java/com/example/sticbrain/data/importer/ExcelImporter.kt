package com.example.sticbrain.data.importer

import java.io.InputStream

/**
 * Interfaz para importación de archivos Excel.
 */
interface ExcelImporter {
    /**
     * Procesa un InputStream de un archivo Excel (.xlsx) y devuelve una lista de filas mapeadas
     * junto con el resultado de la validación.
     */
    suspend fun importFromInputStream(
        inputStream: InputStream
    ): Pair<List<ExcelKnowledgeRow>, ExcelImportResult>
}
