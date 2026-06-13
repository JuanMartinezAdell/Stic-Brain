package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proveedores")
data class ProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val servicioAsociado: String,
    val telefono: String? = null,
    val email: String? = null,
    val web: String? = null,
    val horario: String? = null,
    val sla: String? = null,
    val categoriasRelacionadas: String? = null,
    val notasComentarios: String? = null
)
