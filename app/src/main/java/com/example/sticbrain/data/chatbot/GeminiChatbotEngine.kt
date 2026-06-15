package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.data.model.ChatbotIncidentResult

/**
 * Motor del chatbot que utiliza la API de Gemini para generar respuestas
 * basadas en el contexto local de Stic Brain.
 */
class GeminiChatbotEngine(
    private val apiKey: String,
    private val model: String
) : ChatbotEngine {

    private val apiClient = GeminiApiClient(apiKey, model)

    override suspend fun generateResponse(
        question: String,
        incidencias: List<IncidenciaEntity>
    ): ChatbotEngineResult {
        // Seleccionamos las fichas más relevantes para enviarlas como contexto
        // Reutilizamos la lógica de filtrado del motor local si es necesario,
        // pero aquí simplemente tomamos las primeras 5 por simplicidad en el ejemplo.
        val relevantIncidents = incidencias.take(5)

        val contextString = relevantIncidents.joinToString("\n\n") { ficha ->
            """
            FICHA: ${ficha.tituloNombre}
            CATEGORÍA: ${ficha.categoria}
            PROCEDIMIENTO: ${ficha.procedimientoRespuesta}
            """.trimIndent()
        }

        val answer = apiClient.generateContent(question, contextString)

        // Mapeamos las incidencias para mostrarlas como tarjetas debajo de la respuesta de la IA
        val relatedResults = relevantIncidents.map { ficha ->
            ChatbotIncidentResult(
                incidenciaId = ficha.id,
                tituloNombre = ficha.tituloNombre,
                categoria = ficha.categoria,
                nivelPrioridad = ficha.nivelPrioridad,
                procedimientoResumen = if (ficha.procedimientoRespuesta.length > 100) ficha.procedimientoRespuesta.take(100) + "..." else ficha.procedimientoRespuesta,
                puntuacion = 100 // Relevancia fija para resultados de IA
            )
        }

        return ChatbotEngineResult(
            answer = answer,
            relatedIncidents = relatedResults,
            usedExternalAi = true
        )
    }
}
