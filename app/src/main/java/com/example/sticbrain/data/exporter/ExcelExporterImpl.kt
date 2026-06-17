package com.example.sticbrain.data.exporter

import com.example.sticbrain.data.local.entity.IncidenciaEntity
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

class ExcelExporterImpl : ExcelExporter {

    override suspend fun exportToOutputStream(
        incidencias: List<IncidenciaEntity>,
        outputStream: OutputStream
    ): ExcelExportResult {
        var exportadas = 0
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Incidencias")

            // Crear cabecera
            val headerRow = sheet.createRow(0)
            val headers = listOf(
                "ID", "Categoría", "Título / Nombre", "Descripción", 
                "Frases de Usuario", "Procedimiento / Respuesta", 
                "Palabras clave", "Nivel de prioridad", "Notas / Comentarios",
                "Es Provisional", "Origen", "Revisada"
            )
            
            headers.forEachIndexed { index, title ->
                headerRow.createCell(index).setCellValue(title)
            }

            // Escribir datos
            incidencias.forEachIndexed { index, incidencia ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(incidencia.id.toDouble())
                row.createCell(1).setCellValue(incidencia.categoria)
                row.createCell(2).setCellValue(incidencia.tituloNombre)
                row.createCell(3).setCellValue(incidencia.descripcion)
                row.createCell(4).setCellValue(incidencia.frasesUsuario)
                row.createCell(5).setCellValue(incidencia.procedimientoRespuesta)
                row.createCell(6).setCellValue(incidencia.palabrasClave)
                row.createCell(7).setCellValue(incidencia.nivelPrioridad)
                row.createCell(8).setCellValue(incidencia.notasComentarios ?: "")
                row.createCell(9).setCellValue(if (incidencia.esProvisional) "SÍ" else "NO")
                row.createCell(10).setCellValue(incidencia.origen)
                row.createCell(11).setCellValue(if (incidencia.revisada) "SÍ" else "NO")
                exportadas++
            }

            // Ajustar ancho de columnas (opcional, puede ser lento con muchos datos)
            for (i in headers.indices) {
                sheet.autoSizeColumn(i)
            }

            workbook.write(outputStream)
            workbook.close()

            return ExcelExportResult(
                totalFichas = incidencias.size,
                exportadas = exportadas
            )
        } catch (e: Exception) {
            return ExcelExportResult(
                totalFichas = incidencias.size,
                exportadas = exportadas,
                errorMessage = e.message
            )
        }
    }
}
