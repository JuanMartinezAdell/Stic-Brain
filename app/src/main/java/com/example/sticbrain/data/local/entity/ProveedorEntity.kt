package com.example.sticbrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Contactos
 * 
 * Almacena los datos de contacto y condiciones de escalado para que
 * los técnicos puedan contactar rápidamente con el soporte adecuado.
 */
@Entity(tableName = "proveedores")
data class ProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** Nombre de la empresa o servicio de soporte */
    val nombre: String,
    
    /** Aplicación o sistema al que dan soporte (ej: Soaris HIS) */
    val servicioAsociado: String,
    
    /** Número de teléfono de contacto */
    val telefono: String? = null,
    
    /** Dirección de correo electrónico de soporte */
    val email: String? = null,
    
    /** Sitio web o portal de soporte para abrir tickets */
    val web: String? = null,
    
    /** Días y horas en los que están disponibles (ej: L-V 08-20h) */
    val horario: String? = null,
    
    /** Tiempos de respuesta comprometidos (ej: 4h para críticas) */
    val sla: String? = null,
    
    /** Áreas técnicas relacionadas, separadas por comas */
    val categoriasRelacionadas: String? = null,
    
    /** Cualquier otra información importante para el escalado */
    val notasComentarios: String? = null
)
