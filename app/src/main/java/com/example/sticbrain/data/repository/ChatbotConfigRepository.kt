package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.ChatbotConfigDao
import com.example.sticbrain.data.local.entity.ChatbotConfigEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar la configuración del chatbot.
 *
 * Separa el ViewModel del DAO y mantiene la arquitectura MVVM.
 */
class ChatbotConfigRepository(
    private val chatbotConfigDao: ChatbotConfigDao
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

    suspend fun eliminarApiKey() {
        chatbotConfigDao.eliminarApiKey()
    }

    suspend fun guardarCuentaGoogleConfirmada(email: String, displayName: String?) {
        chatbotConfigDao.guardarCuentaGoogleConfirmada(email, displayName)
    }

    suspend fun eliminarCuentaGoogleConfirmada() {
        chatbotConfigDao.eliminarCuentaGoogleConfirmada()
    }
}
