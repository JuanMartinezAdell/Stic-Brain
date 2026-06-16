package com.example.sticbrain.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sticbrain.data.local.entity.ChatbotConfigEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO encargado de leer y guardar la configuración del chatbot.
 */
@Dao
interface ChatbotConfigDao {

    /**
     * Obtiene la configuración del chatbot.
     */
    @Query("SELECT * FROM chatbot_config WHERE id = 1")
    fun obtenerConfiguracion(): Flow<ChatbotConfigEntity?>

    /**
     * Obtiene la configuración una sola vez.
     */
    @Query("SELECT * FROM chatbot_config WHERE id = 1")
    suspend fun obtenerConfiguracionUnaVez(): ChatbotConfigEntity?

    /**
     * Guarda o sustituye la configuración.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarConfiguracion(config: ChatbotConfigEntity)

    /**
     * Actualiza el estado de la API key en Room.
     */
    @Query("UPDATE chatbot_config SET hasGeminiApiKey = :hasKey, lastUpdated = :timestamp WHERE id = 1")
    suspend fun actualizarEstadoApiKey(hasKey: Boolean, timestamp: Long = System.currentTimeMillis())

    /**
     * Marca la API key como eliminada y vuelve al modo local.
     */
    @Query("UPDATE chatbot_config SET hasGeminiApiKey = 0, mode = 'LOCAL', lastUpdated = :timestamp WHERE id = 1")
    suspend fun marcarApiKeyEliminada(timestamp: Long = System.currentTimeMillis())

    /**
     * Guarda la información de la cuenta Google confirmada.
     */
    @Query("""
        UPDATE chatbot_config 
        SET googleEmail = :email, 
            googleDisplayName = :displayName, 
            googleAccountConfirmed = 1, 
            lastUpdated = :timestamp 
        WHERE id = 1
    """)
    suspend fun guardarCuentaGoogleConfirmada(
        email: String,
        displayName: String?,
        timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Elimina la confirmación de la cuenta Google.
     */
    @Query("""
        UPDATE chatbot_config 
        SET googleEmail = NULL, 
            googleDisplayName = NULL, 
            googleAccountConfirmed = 0, 
            lastUpdated = :timestamp 
        WHERE id = 1
    """)
    suspend fun eliminarCuentaGoogleConfirmada(
        timestamp: Long = System.currentTimeMillis()
    )
}
