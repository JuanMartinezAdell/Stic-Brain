package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.data.model.ChatbotIncidentResult
import com.example.sticbrain.data.model.ChatbotResponseStyle

/**
 * Motor del chatbot que busca respuestas únicamente en la base de datos local.
 */
class LocalChatbotEngine : ChatbotEngine {

    override suspend fun generateResponse(
        question: String,
        incidencias: List<IncidenciaEntity>,
        options: ChatbotGenerationOptions
    ): ChatbotEngineResult {
        if (incidencias.isEmpty()) {
            return ChatbotEngineResult(
                answer = "No hay fichas de conocimiento guardadas en la aplicación."
            )
        }

        val resultados = rankIncidents(question, incidencias)
            .take(options.maxContextIncidents)

        val answerText = buildLocalAnswer(resultados, options.responseStyle)

        return if (resultados.isNotEmpty()) {
            ChatbotEngineResult(
                answer = answerText,
                relatedIncidents = resultados,
                usedExternalAi = false,
                mainIncidentId = resultados.firstOrNull()?.incidenciaId
            )
        } else {
            ChatbotEngineResult(
                answer = "No he encontrado fichas relacionadas con tu consulta localmente.",
                usedExternalAi = false
            )
        }
    }

    /**
     * Construye una respuesta local adaptada al estilo seleccionado.
     */
    private fun buildLocalAnswer(resultados: List<ChatbotIncidentResult>, style: ChatbotResponseStyle): String {
        if (resultados.isEmpty()) return "No hay coincidencias."
        
        val principal = resultados.first()
        
        return when (style) {
            ChatbotResponseStyle.DIAGNOSTICO_BREVE -> """
                Diagnóstico local:
                La causa más probable es '${principal.tituloNombre}'.
                
                Solución principal:
                ${principal.procedimientoResumen}
            """.trimIndent()

            ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO -> """
                He encontrado una ficha técnica relevante:
                '${principal.tituloNombre}'
                
                Procedimiento sugerido:
                ${principal.procedimientoResumen}
                
                (Consulta el detalle completo para ver todos los pasos).
            """.trimIndent()

            ChatbotResponseStyle.RESUMEN_EJECUTIVO -> """
                Se han identificado ${resultados.size} posibles soluciones en la base de conocimiento. 
                La opción más relevante trata sobre '${principal.tituloNombre}' en la categoría ${principal.categoria}.
            """.trimIndent()
        }
    }

    fun rankIncidents(question: String, incidencias: List<IncidenciaEntity>): List<ChatbotIncidentResult> {
        val palabrasClavePregunta = obtenerPalabrasClavePregunta(question)
        if (palabrasClavePregunta.isEmpty()) return emptyList()

        return incidencias
            .asSequence()
            .map { ficha ->
                val (puntos, motivos) = calcularCoincidenciaDetallada(palabrasClavePregunta, ficha, question)
                ficha to (puntos to motivos)
            }
            .filter { it.second.first > 0 }
            .sortedByDescending { it.second.first }
            .map { (ficha, meta) ->
                ficha.toChatbotIncidentResult(meta.first, meta.second)
            }
            .toList()
    }

    private fun calcularCoincidenciaDetallada(palabrasPregunta: List<String>, incidencia: IncidenciaEntity, preguntaOriginal: String): Pair<Int, String> {
        var puntos = 0
        val motivos = mutableListOf<String>()
        val pOriginalNorm = normalizarTexto(preguntaOriginal)
        
        val titulo = normalizarTexto(incidencia.tituloNombre)
        val palabrasClave = normalizarTexto(incidencia.palabrasClave)
        val frasesUsuario = normalizarTexto(incidencia.frasesUsuario)
        val categoria = normalizarTexto(incidencia.categoria)
        val descripcion = normalizarTexto(incidencia.descripcion)
        val procedimiento = normalizarTexto(incidencia.procedimientoRespuesta)

        if (titulo == pOriginalNorm) { puntos += 20; motivos.add("Título idéntico") }

        palabrasPregunta.forEach { palabra ->
            if (titulo.contains(palabra)) { puntos += 8; motivos.add("Título") }
            if (palabrasClave.contains(palabra)) { puntos += 6; motivos.add("Keyword: $palabra") }
            if (frasesUsuario.contains(palabra)) { puntos += 5; motivos.add("Frase usuario") }
            if (categoria.contains(palabra)) { puntos += 4; motivos.add("Categoría") }
            if (descripcion.contains(palabra)) puntos += 3
            if (procedimiento.contains(palabra)) puntos += 3
        }

        if (incidencia.nivelPrioridad == "Urgente" || incidencia.nivelPrioridad == "Crítica") puntos += 2

        return puntos to motivos.distinct().take(3).joinToString(", ")
    }

    private fun obtenerPalabrasClavePregunta(pregunta: String): List<String> {
        val palabrasVacias = setOf("que", "como", "para", "con", "una", "uno", "por", "del", "los", "las", "el", "la", "de", "en", "un", "al", "si", "no", "me", "se", "mi", "hay", "donde", "cuando", "cual")
        return normalizarTexto(pregunta).split(" ").filter { it.length >= 3 }.filter { it !in palabrasVacias }.distinct()
    }

    private fun normalizarTexto(texto: String): String {
        return texto.lowercase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("ü", "u").replace("ñ", "n").replace(Regex("[^a-z0-9\\s]"), " ").replace(Regex("\\s+"), " ").trim()
    }

    private fun IncidenciaEntity.toChatbotIncidentResult(puntuacion: Int, motivos: String): ChatbotIncidentResult {
        return ChatbotIncidentResult(
            incidenciaId = id,
            tituloNombre = tituloNombre,
            categoria = categoria,
            nivelPrioridad = nivelPrioridad,
            procedimientoResumen = if (procedimientoRespuesta.length > 150) procedimientoRespuesta.take(150) + "..." else procedimientoRespuesta,
            puntuacion = puntuacion,
            motivoCoincidencia = motivos
        )
    }
}
