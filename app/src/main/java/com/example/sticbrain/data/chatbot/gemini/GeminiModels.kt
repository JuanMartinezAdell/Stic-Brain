package com.example.sticbrain.data.chatbot.gemini

import kotlinx.serialization.Serializable

/**
 * Modelos de datos para la API de Gemini (Google Generative AI).
 */

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = null
)

@Serializable
data class GeminiGenerationConfig(
    val temperature: Double = 0.2,
    val topP: Double = 0.8,
    val topK: Int = 40,
    val maxOutputTokens: Int = 1024
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>
)

@Serializable
data class GeminiPart(
    val text: String
)

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContentResponse? = null
)

@Serializable
data class GeminiContentResponse(
    val parts: List<GeminiPartResponse>? = null
)

@Serializable
data class GeminiPartResponse(
    val text: String? = null
)
