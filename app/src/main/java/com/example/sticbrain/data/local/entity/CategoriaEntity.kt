package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val descripcion: String? = null,
    val activa: Boolean = true,
    val fechaCreacion: Long = System.currentTimeMillis()
)
