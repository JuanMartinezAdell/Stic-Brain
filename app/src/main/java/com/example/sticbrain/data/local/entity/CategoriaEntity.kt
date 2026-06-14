package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Categoría técnica en la base de datos.
 * 
 * Sirve para clasificar las fichas de conocimiento y organizar
 * la base de datos de forma jerárquica por áreas de soporte.
 */
@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** Nombre de la categoría (ej: Acceso y autenticación) */
    val nombre: String,
    
    /** Breve descripción del tipo de incidencias que engloba */
    val descripcion: String? = null,
    
    /** Indica si la categoría está disponible para ser usada */
    val activa: Boolean = true,
    
    /** Fecha de creación de la categoría */
    val fechaCreacion: Long = System.currentTimeMillis()
)
