package com.example.sticbrain.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sticbrain.data.local.dao.*
import com.example.sticbrain.data.local.entity.*

@Database(
    entities = [
        IncidenciaEntity::class,
        CategoriaEntity::class,
        AplicacionEntity::class,
        EntornoEntity::class,
        ProveedorEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SticBrainDatabase : RoomDatabase() {

    abstract fun incidenciaDao(): IncidenciaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun aplicacionDao(): AplicacionDao
    abstract fun entornoDao(): EntornoDao
    abstract fun proveedorDao(): ProveedorDao

    companion object {
        @Volatile
        private var INSTANCE: SticBrainDatabase? = null

        fun getDatabase(context: Context): SticBrainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SticBrainDatabase::class.java,
                    "stic_brain_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
