package com.example.sticbrain.data.importer

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.text.Normalizer

class ExcelImporterImpl : ExcelImporter {

    override suspend fun importFromInputStream(
        inputStream: InputStream
    ): Pair<List<ExcelKnowledgeRow>, ExcelImportResult> {
        val rows = mutableListOf<ExcelKnowledgeRow>()
        val errors = mutableListOf<String>()
        var validCount = 0
        var invalidCount = 0
        var totalProcessed = 0

        try {
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)
            val headerRow = sheet.getRow(0) ?: throw Exception("El archivo Excel está vacío")

            // Mapeo de cabeceras
            val headerMap = mutableMapOf<String, Int>()
            for (i in 0 until headerRow.lastCellNum.toInt()) {
                val cellValue = headerRow.getCell(i)?.toString() ?: ""
                if (cellValue.isNotBlank()) {
                    headerMap[normalizeHeader(cellValue)] = i
                }
            }

            // Procesar filas (empezando desde la 1)
            for (rowIndex in 1..sheet.lastRowNum) {
                val row = sheet.getRow(rowIndex) ?: continue
                totalProcessed++

                val knowledgeRow = ExcelKnowledgeRow(
                    idOriginal = getCellValue(row, headerMap, "id"),
                    categoria = getCellValue(row, headerMap, "categoria") ?: "Sin categoría",
                    tituloNombre = getCellValue(row, headerMap, "titulonombre") ?: getCellValue(row, headerMap, "titulo") ?: "",
                    descripcion = getCellValue(row, headerMap, "descripcion") ?: "",
                    frasesUsuario = getCellValue(row, headerMap, "frasesusuario") ?: "",
                    procedimientoRespuesta = getCellValue(row, headerMap, "procedimientorespuesta") ?: getCellValue(row, headerMap, "procedimiento") ?: "",
                    palabrasClave = getCellValue(row, headerMap, "palabrasclave") ?: "",
                    nivelPrioridad = getCellValue(row, headerMap, "nivelprioridad") ?: getCellValue(row, headerMap, "prioridad") ?: "Normal",
                    notasComentarios = getCellValue(row, headerMap, "notascomentarios") ?: getCellValue(row, headerMap, "notas")
                )

                val error = ExcelKnowledgeValidator.validateRow(knowledgeRow, rowIndex + 1)
                if (error == null) {
                    rows.add(knowledgeRow)
                    validCount++
                } else {
                    errors.add(error)
                    invalidCount++
                }
            }
            workbook.close()
        } catch (e: Exception) {
            errors.add("Error crítico al procesar Excel: ${e.message}")
        }

        return Pair(
            rows,
            ExcelImportResult(
                totalFilas = totalProcessed,
                filasValidas = validCount,
                filasInvalidas = invalidCount,
                errores = errors
            )
        )
    }

    private fun normalizeHeader(value: String): String {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .replace("[^a-z]".toRegex(), "")
    }

    private fun getCellValue(row: Row, headerMap: Map<String, Int>, key: String): String? {
        val colIndex = headerMap[key] ?: return null
        val cell = row.getCell(colIndex) ?: return null
        
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> {
                val value = cell.numericCellValue
                if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
            }
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.FORMULA -> cell.toString()
            else -> null
        }
    }
}
