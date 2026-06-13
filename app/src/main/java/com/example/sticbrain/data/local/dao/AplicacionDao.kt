package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.AplicacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AplicacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aplicacion: AplicacionEntity)

    @Update
    suspend fun update(aplicacion: AplicacionEntity)

    @Delete
    suspend fun delete(aplicacion: AplicacionEntity)

    @Query("SELECT * FROM aplicaciones WHERE id = :id")
    suspend fun getById(id: Long): AplicacionEntity?

    @Query("SELECT * FROM aplicaciones ORDER BY nombre ASC")
    fun getAll(): Flow<List<AplicacionEntity>>
}
