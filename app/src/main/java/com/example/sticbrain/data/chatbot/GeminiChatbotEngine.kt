package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.local.entity.IncidenciaEntity

/**
 * Motor avanzado del chatbot que utiliza Gemini para generar soluciones unificadas.
 */
class GeminiChatbotEngine(
    apiKey: String,
    model: String
) : ChatbotEngine {

    private val apiClient = GeminiApiClient(apiKey, model)
    private val localEngine = LocalChatbotEngine()

    override suspend fun generateResponse(
        question: String,
        incidencias: List<IncidenciaEntity>,
        options: ChatbotGenerationOptions
    ): ChatbotEngineResult {
        
        // 1. Ranking local
        val rankedIncidents = localEngine.rankIncidents(question, incidencias)
        
        if (rankedIncidents.isEmpty()) {
            return ChatbotEngineResult(
                answer = "No he encontrado fichas técnicas relacionadas con tu consulta.",
                usedExternalAi = false
            )
        }

        val topIncidents = rankedIncidents.take(options.maxContextIncidents)
        
        // 2. Contexto enriquecido
        val fullIncidents = topIncidents.mapNotNull { result ->
            incidencias.find { it.id == result.incidenciaId }
        }
        
        val context = buildGeminiContext(fullIncidents)

        // 3. Llamada a Gemini
        val geminiAnswer = apiClient.generateContent(question, context, options)

        // 4. Resultado estructurado
        return ChatbotEngineResult(
            answer = geminiAnswer,
            relatedIncidents = topIncidents,
            usedExternalAi = true,
            confidence = extraerNivelConfianza(geminiAnswer),
            mainIncidentId = topIncidents.firstOrNull()?.incidenciaId,
            alternativeIncidentIds = topIncidents.drop(1).map { it.incidenciaId }
        )
    }

    private fun buildGeminiContext(incidencias: List<IncidenciaEntity>): String {
        return incidencias.joinToString("\n\n") { ficha ->
            """
            FICHA TÉCNICA [ID: ${ficha.id}]
            Título: ${ficha.tituloNombre}
            Categoría: ${ficha.categoria}
            Prioridad: ${ficha.nivelPrioridad}
            Palabras clave: ${ficha.palabrasClave}
            Descripción: ${ficha.descripcion}
            PROCEDIMIENTO:
            ${ficha.procedimientoRespuesta}
            ---
            """.trimIndent()
        }
    }

    private fun extraerNivelConfianza(answer: String): String? {
        val normalized = answer.lowercase()
        return when {
            "nivel de confianza: alta" in normalized || "confianza: alta" in normalized -> "Alta"
            "nivel de confianza: media" in normalized || "confianza: media" in normalized -> "Media"
            "nivel de confianza: baja" in normalized || "confianza: baja" in normalized -> "Baja"
            else -> null
        }
    }
}
