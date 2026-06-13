package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(proveedor: ProveedorEntity)

    @Update
    suspend fun actualizar(proveedor: ProveedorEntity)

    @Delete
    suspend fun eliminar(proveedor: ProveedorEntity)

    @Query("SELECT * FROM proveedores WHERE id = :id")
    fun obtenerProveedorPorId(id: Long): Flow<ProveedorEntity?>

    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun obtenerProveedores(): Flow<List<ProveedorEntity>>

    @Query("""
        SELECT * FROM proveedores 
        WHERE nombre LIKE '%' || :query || '%' 
        OR servicioAsociado LIKE '%' || :query || '%'
        OR categoriasRelacionadas LIKE '%' || :query || '%'
        ORDER BY nombre ASC
    """)
    fun buscarProveedores(query: String): Flow<List<ProveedorEntity>>
}
