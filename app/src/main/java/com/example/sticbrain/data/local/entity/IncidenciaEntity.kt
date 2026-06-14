package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Incidencias
 * 
 * Esta clase define la estructura de la tabla "incidencias", alineada con
 * los campos requeridos por la plantilla Excel de la base de conocimiento TIC.
 */
@Entity(tableName = "incidencias")
data class IncidenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** Categoría a la que pertenece la ficha (ej: Hardware, Software) */
    val categoria: String,
    
    /** Título corto y descriptivo de la ficha */
    val tituloNombre: String,
    
    /** Explicación detallada del problema o situación */
    val descripcion: String,
    
    /** Texto literal de lo que suele decir el usuario (ej: "No puedo entrar") */
    val frasesUsuario: String,
    
    /** Pasos técnicos numerados para resolver la situación */
    val procedimientoRespuesta: String,
    
    /** Etiquetas separadas por comas para facilitar la búsqueda */
    val palabrasClave: String,
    
    /** Importancia de la ficha: Baja, Normal, Alta, Crítica */
    val nivelPrioridad: String,
    
    /** Comentarios adicionales u observaciones técnicas */
    val notasComentarios: String? = null,
    
    /** Marca de tiempo de cuándo se creó la ficha */
    val fechaCreacion: Long = System.currentTimeMillis(),
    
    /** Marca de tiempo de la última vez que se editó */
    val fechaModificacion: Long? = null
)
