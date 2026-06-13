package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

/**
 * Operaciones para la tabla de proveedores y servicios de soporte.
 */
@Dao
interface ProveedorDao {
    /** Guarda un nuevo proveedor de soporte. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(proveedor: ProveedorEntity)

    /** Actualiza los datos de contacto o condiciones de un proveedor. */
    @Update
    suspend fun actualizar(proveedor: ProveedorEntity)

    /** Elimina un proveedor del directorio. */
    @Delete
    suspend fun eliminar(proveedor: ProveedorEntity)

    /** Recupera los datos completos de un proveedor por su identificador. */
    @Query("SELECT * FROM proveedores WHERE id = :id")
    fun obtenerProveedorPorId(id: Long): Flow<ProveedorEntity?>

    /** Obtiene la lista completa de proveedores ordenada alfabéticamente. */
    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun obtenerProveedores(): Flow<List<ProveedorEntity>>

    /**
     * Busca proveedores filtrando por nombre, servicio o categorías relacionadas.
     */
    @Query("""
        SELECT * FROM proveedores 
        WHERE nombre LIKE '%' || :query || '%' 
        OR servicioAsociado LIKE '%' || :query || '%'
        OR categoriasRelacionadas LIKE '%' || :query || '%'
        ORDER BY nombre ASC
    """)
    fun buscarProveedores(query: String): Flow<List<ProveedorEntity>>
}
