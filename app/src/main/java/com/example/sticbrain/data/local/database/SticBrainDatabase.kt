package com.example.sticbrain.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sticbrain.data.local.dao.*
import com.example.sticbrain.data.local.entity.*

/**
 * Base de datos principal de la aplicación Stic Brain.
 * 
 * Aquí se definen las tablas (entidades) y se proporcionan los accesos
 * a los objetos DAO para realizar operaciones en cada tabla.
 */
@Database(
    entities = [
        IncidenciaEntity::class,
        CategoriaEntity::class,
        ProveedorEntity::class,
        ChatMessageEntity::class,
        ChatbotConfigEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class SticBrainDatabase : RoomDatabase() {

    /** Acceso a las fichas de conocimiento (incidencias) */
    abstract fun incidenciaDao(): IncidenciaDao
    
    /** Acceso a las categorías de clasificación */
    abstract fun categoriaDao(): CategoriaDao
    
    /** Acceso a los proveedores de soporte técnico */
    abstract fun proveedorDao(): ProveedorDao

    /** Acceso al historial de mensajes del chatbot */
    abstract fun chatMessageDao(): ChatMessageDao

    /** Acceso a la configuración del chatbot */
    abstract fun chatbotConfigDao(): ChatbotConfigDao

    companion object {
        @Volatile
        private var INSTANCE: SticBrainDatabase? = null

        /**
         * Crea u obtiene la instancia única de la base de datos (Singleton).
         */
        fun getDatabase(context: Context): SticBrainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SticBrainDatabase::class.java,
                    "stic_brain_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
