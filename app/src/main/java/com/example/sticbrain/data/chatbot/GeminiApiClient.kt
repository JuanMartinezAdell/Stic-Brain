package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.chatbot.gemini.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Cliente para interactuar con la API REST de Google Gemini.
 */
class GeminiApiClient(
    private val apiKey: String,
    private val model: String
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Realiza una petición de generación de contenido a Gemini.
     */
    suspend fun generateContent(question: String, context: String): String = withContext(Dispatchers.IO) {
        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val prompt = """
                Eres el asistente técnico de Stic Brain.
                Responde en español de forma clara y profesional.
                Usa únicamente el contexto de fichas de conocimiento proporcionado a continuación.
                Si no hay información suficiente en el contexto para responder, indícalo educadamente.
                No inventes procedimientos ni proporciones información externa al contexto.

                CONTEXTO DE FICHAS:
                $context

                PREGUNTA DEL USUARIO:
                $question
            """.trimIndent()

            val requestBody = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                )
            )

            val bodyJson = json.encodeToString(requestBody)
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val request = Request.Builder()
                .url(url)
                .post(bodyJson.toRequestBody(mediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val geminiResponse = json.decodeFromString<GeminiResponse>(responseBody)
                return@withContext geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Gemini no devolvió una respuesta válida."
            } else {
                return@withContext "Error en la API de Gemini: ${response.code} ${response.message}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Se produjo un error al conectar con Gemini: ${e.localizedMessage}"
        }
    }
}
