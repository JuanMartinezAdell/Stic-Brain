package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(categoria: CategoriaEntity)

    @Update
    suspend fun actualizar(categoria: CategoriaEntity)

    @Delete
    suspend fun eliminar(categoria: CategoriaEntity)

    @Query("SELECT * FROM categorias WHERE id = :id")
    fun obtenerCategoriaPorId(id: Long): Flow<CategoriaEntity?>

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun obtenerCategorias(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categorias WHERE activa = 1 ORDER BY nombre ASC")
    fun obtenerCategoriasActivas(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categorias WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarCategorias(query: String): Flow<List<CategoriaEntity>>

    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun contarCategorias(): Int
}
