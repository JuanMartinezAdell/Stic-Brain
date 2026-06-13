package com.example.sticbrain.data.importer

import java.io.InputStream

/**
 * Implementación inicial del importador de Excel.
 * La lectura real se implementará en el siguiente paso (Prompt 32) usando una librería compatible.
 */
class ExcelImporterImpl : ExcelImporter {

    override suspend fun importFromInputStream(
        inputStream: InputStream
    ): Pair<List<ExcelKnowledgeRow>, ExcelImportResult> {
        // REGLAS DE CORRESPONDENCIA DE COLUMNAS (REFERENCIA)
        // -------------------------------------------------------------
        // Columna Excel                       Campo en IncidenciaEntity
        // -------------------------------------------------------------
        // ID                                  idOriginal o ignorado
        // Categoría                           categoria
        // Título / Nombre                     tituloNombre
        // Descripción                         descripcion
        // Frases de Usuario                   frasesUsuario
        // Procedimiento / Respuesta           procedimientoRespuesta
        // Palabras clave                      palabrasClave
        // Nivel de prioridad                  nivelPrioridad
        // Notas / Comentarios                 notasComentarios

        // TODO: Implementar lectura real de Excel (Apache POI u otra compatible) en el Prompt 32.
        
        return Pair(
            emptyList(),
            ExcelImportResult(
                totalFilas = 0,
                filasValidas = 0,
                filasInvalidas = 0,
                errores = listOf("Importación Excel pendiente de implementar en el siguiente paso")
            )
        )
    }
}
