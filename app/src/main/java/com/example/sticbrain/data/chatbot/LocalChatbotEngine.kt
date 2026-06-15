package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.data.model.ChatbotIncidentResult

/**
 * Motor del chatbot que busca respuestas únicamente en la base de datos local.
 * 
 * Utiliza un algoritmo de puntuación por relevancia basado en palabras clave.
 */
class LocalChatbotEngine : ChatbotEngine {

    private val maxResultados = 3

    override suspend fun generateResponse(
        question: String,
        incidencias: List<IncidenciaEntity>
    ): ChatbotEngineResult {
        if (incidencias.isEmpty()) {
            return ChatbotEngineResult(
                answer = "No hay fichas de conocimiento guardadas en la aplicación. Por favor, añade información para que pueda ayudarte."
            )
        }

        val palabrasClavePregunta = obtenerPalabrasClavePregunta(question)
        if (palabrasClavePregunta.isEmpty()) {
            return ChatbotEngineResult(
                answer = "No he podido identificar palabras clave en tu pregunta. Prueba a ser más específico (ej: 'red', 'impresora')."
            )
        }

        val resultados = incidencias
            .asSequence()
            .map { ficha ->
                val puntos = calcularCoincidencia(palabrasClavePregunta, ficha)
                ficha to puntos
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(maxResultados)
            .map { (ficha, puntos) ->
                ficha.toChatbotIncidentResult(puntos)
            }
            .toList()

        return if (resultados.isNotEmpty()) {
            ChatbotEngineResult(
                answer = "He encontrado estas fichas relacionadas en la base de conocimiento:",
                relatedIncidents = resultados,
                usedExternalAi = false
            )
        } else {
            ChatbotEngineResult(
                answer = "No he encontrado fichas relacionadas con tu consulta.\n\nPrueba con otras palabras clave o crea una nueva ficha si se trata de un procedimiento nuevo.",
                usedExternalAi = false
            )
        }
    }

    private fun calcularCoincidencia(palabrasPregunta: List<String>, incidencia: IncidenciaEntity): Int {
        var puntos = 0
        val titulo = normalizarTexto(incidencia.tituloNombre)
        val palabrasClave = normalizarTexto(incidencia.palabrasClave)
        val frasesUsuario = normalizarTexto(incidencia.frasesUsuario)
        val descripcion = normalizarTexto(incidencia.descripcion)
        val procedimiento = normalizarTexto(incidencia.procedimientoRespuesta)
        val categoria = normalizarTexto(incidencia.categoria)

        palabrasPregunta.forEach { palabra ->
            if (titulo.contains(palabra)) puntos += 5
            if (palabrasClave.contains(palabra)) puntos += 4
            if (frasesUsuario.contains(palabra)) puntos += 3
            if (descripcion.contains(palabra)) puntos += 2
            if (procedimiento.contains(palabra)) puntos += 2
            if (categoria.contains(palabra)) puntos += 2
        }
        return puntos
    }

    private fun obtenerPalabrasClavePregunta(pregunta: String): List<String> {
        val palabrasVacias = setOf("que", "como", "para", "con", "una", "uno", "por", "del", "los", "las", "el", "la", "de", "en", "un", "al", "si", "no", "me", "se", "mi")
        return normalizarTexto(pregunta)
            .split(" ")
            .filter { it.length >= 3 }
            .filter { it !in palabrasVacias }
            .distinct()
    }

    private fun normalizarTexto(texto: String): String {
        return texto.lowercase()
            .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
            .replace("ü", "u").replace("ñ", "n")
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .replace(Regex("\\s+"), " ").trim()
    }

    private fun IncidenciaEntity.toChatbotIncidentResult(puntuacion: Int): ChatbotIncidentResult {
        val resumen = if (procedimientoRespuesta.length > 150) procedimientoRespuesta.take(150) + "..." else procedimientoRespuesta
        return ChatbotIncidentResult(
            incidenciaId = id,
            tituloNombre = tituloNombre,
            categoria = categoria,
            nivelPrioridad = nivelPrioridad,
            procedimientoResumen = resumen,
            puntuacion = puntuacion
        )
    }
}
