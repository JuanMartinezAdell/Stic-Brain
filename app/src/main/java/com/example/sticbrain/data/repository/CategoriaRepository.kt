package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.CategoriaDao
import com.example.sticbrain.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las categorías.
 */
class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    /** Obtiene todas las categorías almacenadas. */
    fun obtenerCategorias(): Flow<List<CategoriaEntity>> = categoriaDao.obtenerCategorias()

    /** Obtiene solo aquellas categorías marcadas como "activa". */
    fun obtenerCategoriasActivas(): Flow<List<CategoriaEntity>> = categoriaDao.obtenerCategoriasActivas()

    /** Recupera una categoría específica por su identificador. */
    fun obtenerCategoriaPorId(id: Long): Flow<CategoriaEntity?> = categoriaDao.obtenerCategoriaPorId(id)

    /** Busca categorías por su nombre. */
    fun buscarCategorias(query: String): Flow<List<CategoriaEntity>> = categoriaDao.buscarCategorias(query)

    /** Registra una nueva categoría en la base de datos. */
    suspend fun insertarCategoria(categoria: CategoriaEntity) {
        categoriaDao.insertar(categoria)
    }

    /** Modifica el nombre o estado de una categoría. */
    suspend fun actualizarCategoria(categoria: CategoriaEntity) {
        categoriaDao.actualizar(categoria)
    }

    /** Borra una categoría. */
    suspend fun eliminarCategoria(categoria: CategoriaEntity) {
        categoriaDao.eliminar(categoria)
    }

    /** Devuelve cuántas categorías hay registradas actualmente. */
    suspend fun contarCategorias(): Int = categoriaDao.contarCategorias()
}
