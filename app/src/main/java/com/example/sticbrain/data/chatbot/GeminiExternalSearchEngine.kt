package com.example.sticbrain.data.chatbot

/**
 * Motor encargado de realizar búsquedas generales cuando no hay contexto local suficiente.
 */
class GeminiExternalSearchEngine(
    private val apiClient: GeminiApiClient
) {
    suspend fun generateExternalSolution(
        question: String,
        options: ChatbotGenerationOptions
    ): ChatbotEngineResult {
        val answer = apiClient.generateExternalContent(
            question = question,
            options = options
        )

        return ChatbotEngineResult(
            answer = answer,
            relatedIncidents = emptyList(),
            usedExternalAi = true,
            confidence = "Baja",
            hasEnoughLocalInformation = false,
            needsExternalSearchConfirmation = false
        )
    }
}
