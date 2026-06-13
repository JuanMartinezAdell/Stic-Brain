package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidencias")
data class IncidenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoria: String,
    val tituloNombre: String,
    val descripcion: String,
    val frasesUsuario: String,
    val procedimientoRespuesta: String,
    val palabrasClave: String,
    val nivelPrioridad: String,
    val notasComentarios: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaModificacion: Long? = null
)
