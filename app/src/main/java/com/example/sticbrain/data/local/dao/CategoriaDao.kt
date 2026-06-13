package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoria: CategoriaEntity)

    @Update
    suspend fun update(categoria: CategoriaEntity)

    @Delete
    suspend fun delete(categoria: CategoriaEntity)

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun getById(id: Long): CategoriaEntity?

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAll(): Flow<List<CategoriaEntity>>
}
