package com.example.sticbrain.data.chatbot

import com.example.sticbrain.data.chatbot.gemini.*
import com.example.sticbrain.data.model.ChatbotDetailLevel
import com.example.sticbrain.data.model.ChatbotResponseStyle
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
 * Cliente avanzado para interactuar con la API REST de Google Gemini.
 * 
 * Implementa una configuración de generación optimizada para diagnósticos técnicos
 * y construye prompts estructurados para forzar respuestas profesionales.
 * 
 * Ahora adapta el prompt según el estilo y nivel de detalle configurado.
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
     * Realiza una petición de generación de contenido estructurado a Gemini.
     */
    suspend fun generateContent(
        question: String, 
        context: String,
        options: ChatbotGenerationOptions
    ): String = withContext(Dispatchers.IO) {
        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val prompt = """
                Eres el asistente técnico de Stic Brain, una base de conocimiento TIC.
                
                Tu tarea es analizar la pregunta del usuario y el contexto de fichas proporcionado para proponer la solución más probable.

                Reglas obligatorias:
                1. Responde siempre en español.
                2. Usa únicamente la información incluida en las fichas proporcionadas.
                3. No inventes procedimientos, credenciales, comandos ni datos que no aparezcan en el contexto.
                4. Si varias fichas tratan el mismo problema, unifica la información en una solución coherente.
                5. Si una ficha parece claramente más adecuada, priorízala.
                6. Si la información no es suficiente, dilo claramente.
                7. Indica un nivel de confianza: Alta, Media o Baja.
                8. Menciona las fichas usadas como referencia por título o ID.
                9. No muestres datos sensibles aunque aparezcan en el contexto.

                Estilo de respuesta configurado:
                ${buildStyleInstruction(options.responseStyle)}

                Nivel de detalle configurado:
                ${buildDetailInstruction(options.detailLevel)}

                Formato de respuesta esperado:

                Solución más probable:
                [Contenido según estilo y detalle]

                Motivo:
                [Explica brevemente la relación con las fichas]

                Pasos recomendados:
                [Lista de pasos según estilo]

                Nivel de confianza:
                [Alta / Media / Baja]

                Fichas usadas como referencia:
                - [ID y título]

                Alternativas si no funciona:
                - [Opcional]

                PREGUNTA DEL USUARIO:
                $question

                CONTEXTO DE FICHAS (Base de conocimiento):
                $context
            """.trimIndent()

            val requestBody = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.2,
                    topP = 0.8,
                    maxOutputTokens = 2048
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
            return@withContext "Error de conexión: ${e.localizedMessage}"
        }
    }

    private fun buildStyleInstruction(style: ChatbotResponseStyle): String {
        return when (style) {
            ChatbotResponseStyle.DIAGNOSTICO_BREVE -> """
                Usa un estilo de diagnóstico breve.
                Responde de forma directa y concisa.
                Prioriza la causa raíz más probable y la solución inmediata.
                Evita introducciones largas.
            """.trimIndent()

            ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO -> """
                Usa un estilo de procedimiento paso a paso.
                Organiza la solución en pasos lógicos y numerados.
                Asegúrate de que cada paso sea una acción clara.
            """.trimIndent()

            ChatbotResponseStyle.RESUMEN_EJECUTIVO -> """
                Usa un estilo de resumen ejecutivo.
                Explica la situación y la solución de forma profesional y clara.
                Ideal para comunicar el estado de una incidencia sin entrar en excesivos tecnicismos.
            """.trimIndent()
        }
    }

    private fun buildDetailInstruction(level: ChatbotDetailLevel): String {
        return when (level) {
            ChatbotDetailLevel.BAJO -> """
                Nivel de detalle bajo.
                Sé lo más breve posible.
                Solo los pasos críticos para resolver el problema.
            """.trimIndent()

            ChatbotDetailLevel.MEDIO -> """
                Nivel de detalle medio.
                Proporciona una explicación equilibrada.
                Incluye los pasos principales y alguna comprobación básica.
            """.trimIndent()

            ChatbotDetailLevel.ALTO -> """
                Nivel de detalle alto.
                Explicación exhaustiva.
                Incluye diagnóstico detallado, pasos minuciosos, comprobaciones de seguridad,
                posibles efectos secundarios y múltiples alternativas.
            """.trimIndent()
        }
    }
}
