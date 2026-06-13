package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(proveedor: ProveedorEntity)

    @Update
    suspend fun update(proveedor: ProveedorEntity)

    @Delete
    suspend fun delete(proveedor: ProveedorEntity)

    @Query("SELECT * FROM proveedores WHERE id = :id")
    suspend fun getById(id: Long): ProveedorEntity?

    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun getAll(): Flow<List<ProveedorEntity>>
}
