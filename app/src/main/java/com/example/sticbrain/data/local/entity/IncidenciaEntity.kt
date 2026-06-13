package com.example.sticbrain.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "incidencias",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AplicacionEntity::class,
            parentColumns = ["id"],
            childColumns = ["aplicacionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntornoEntity::class,
            parentColumns = ["id"],
            childColumns = ["entornoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProveedorEntity::class,
            parentColumns = ["id"],
            childColumns = ["proveedorId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["titulo"]),
        Index(value = ["mensajeError"]),
        Index(value = ["categoriaId"]),
        Index(value = ["aplicacionId"]),
        Index(value = ["entornoId"]),
        Index(value = ["proveedorId"])
    ]
)
data class IncidenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val descripcion: String,
    @ColumnInfo(name = "mensajeError")
    val mensajeError: String? = null,
    val causa: String,
    val solucion: String,
    val categoriaId: Long,
    val aplicacionId: Long,
    val entornoId: Long,
    val proveedorId: Long? = null,
    val prioridad: Int, // 1: Baja, 2: Media, 3: Alta, 4: Crítica
    val estado: String, // Ejemplo: "Resuelta", "En proceso", "Documentada"
    val fechaCreacion: Long,
    val fechaModificacion: Long,
    val valoracionUtilidad: Int // 1 a 5
)
