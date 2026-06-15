package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.ChatMessageDao
import com.example.sticbrain.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio del historial del chatbot.
 *
 * Esta clase separa el ViewModel del DAO. De esta forma, el ViewModel
 * no necesita conocer los detalles internos de Room.
 */
class ChatMessageRepository(
    private val chatMessageDao: ChatMessageDao
) {

    /**
     * Obtiene el historial completo de mensajes como Flow.
     */
    fun obtenerHistorial(): Flow<List<ChatMessageEntity>> {
        return chatMessageDao.obtenerHistorial()
    }

    /**
     * Guarda un mensaje individual en Room.
     */
    suspend fun insertarMensaje(message: ChatMessageEntity) {
        chatMessageDao.insertarMensaje(message)
    }

    /**
     * Borra todo el historial del chatbot.
     */
    suspend fun limpiarHistorial() {
        chatMessageDao.limpiarHistorial()
    }
}
