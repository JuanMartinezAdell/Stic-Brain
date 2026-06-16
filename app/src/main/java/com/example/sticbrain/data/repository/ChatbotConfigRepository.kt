package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.ChatbotConfigDao
import com.example.sticbrain.data.local.entity.ChatbotConfigEntity
import com.example.sticbrain.data.security.SecureApiKeyStorage
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar la configuración del chatbot.
 * 
 * Combina la persistencia en Room para ajustes generales y SecureApiKeyStorage
 * para el almacenamiento cifrado de claves sensibles.
 */
class ChatbotConfigRepository(
    private val chatbotConfigDao: ChatbotConfigDao,
    private val secureApiKeyStorage: SecureApiKeyStorage
) {

    fun obtenerConfiguracion(): Flow<ChatbotConfigEntity?> {
        return chatbotConfigDao.obtenerConfiguracion()
    }

    suspend fun obtenerConfiguracionUnaVez(): ChatbotConfigEntity? {
        return chatbotConfigDao.obtenerConfiguracionUnaVez()
    }

    suspend fun guardarConfiguracion(config: ChatbotConfigEntity) {
        chatbotConfigDao.guardarConfiguracion(config)
    }

    /**
     * Guarda la API key de forma cifrada y actualiza el estado en Room.
     */
    suspend fun guardarGeminiApiKey(apiKey: String) {
        secureApiKeyStorage.saveGeminiApiKey(apiKey)
        chatbotConfigDao.actualizarEstadoApiKey(hasKey = true)
    }

    /**
     * Obtiene la API key desde el almacenamiento cifrado.
     */
    fun obtenerGeminiApiKey(): String? {
        return secureApiKeyStorage.getGeminiApiKey()
    }

    /**
     * Elimina la clave cifrada y actualiza Room.
     */
    suspend fun eliminarGeminiApiKey() {
        secureApiKeyStorage.clearGeminiApiKey()
        chatbotConfigDao.marcarApiKeyEliminada()
    }

    fun existeGeminiApiKey(): Boolean {
        return secureApiKeyStorage.hasGeminiApiKey()
    }

    suspend fun guardarCuentaGoogleConfirmada(email: String, displayName: String?) {
        chatbotConfigDao.guardarCuentaGoogleConfirmada(email, displayName)
    }

    suspend fun eliminarCuentaGoogleConfirmada() {
        chatbotConfigDao.eliminarCuentaGoogleConfirmada()
    }
}
