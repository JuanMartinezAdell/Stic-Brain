package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Operaciones para la tabla de categorías
 */
@Dao
interface CategoriaDao {
    /** Guarda una nueva categoría. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(categoria: CategoriaEntity)

    /** Cambia los datos de una categoría. */
    @Update
    suspend fun actualizar(categoria: CategoriaEntity)

    /** Borra una categoría de la lista. */
    @Delete
    suspend fun eliminar(categoria: CategoriaEntity)

    /** Recupera los datos de una categoría por su ID. */
    @Query("SELECT * FROM categorias WHERE id = :id")
    fun obtenerCategoriaPorId(id: Long): Flow<CategoriaEntity?>

    /** Lista todas las categorías por orden alfabético. */
    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun obtenerCategorias(): Flow<List<CategoriaEntity>>

    /** Lista solo las categorías que están marcadas como activas. */
    @Query("SELECT * FROM categorias WHERE activa = 1 ORDER BY nombre ASC")
    fun obtenerCategoriasActivas(): Flow<List<CategoriaEntity>>

    /** Busca categorías por nombre. */
    @Query("SELECT * FROM categorias WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarCategorias(query: String): Flow<List<CategoriaEntity>>

    /** Cuenta el número total de categorías guardadas. */
    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun contarCategorias(): Int
}
