package com.example.sticbrain.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sticbrain.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO encargado de acceder a la tabla de mensajes del chatbot.
 *
 * Aquí se definen las operaciones necesarias para guardar mensajes,
 * obtener el historial y limpiar la conversación.
 */
@Dao
interface ChatMessageDao {

    /**
     * Obtiene todos los mensajes guardados ordenados por fecha.
     *
     * Se devuelve como Flow para que la pantalla pueda actualizarse
     * automáticamente cuando se inserten nuevos mensajes.
     */
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun obtenerHistorial(): Flow<List<ChatMessageEntity>>

    /**
     * Inserta un mensaje en el historial del chatbot.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMensaje(message: ChatMessageEntity)

    /**
     * Elimina todos los mensajes del historial.
     */
    @Query("DELETE FROM chat_messages")
    suspend fun limpiarHistorial()
}
