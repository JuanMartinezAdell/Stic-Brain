package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.EntornoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntornoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entorno: EntornoEntity)

    @Update
    suspend fun update(entorno: EntornoEntity)

    @Delete
    suspend fun delete(entorno: EntornoEntity)

    @Query("SELECT * FROM entornos WHERE id = :id")
    suspend fun getById(id: Long): EntornoEntity?

    @Query("SELECT * FROM entornos ORDER BY nombre ASC")
    fun getAll(): Flow<List<EntornoEntity>>
}
