package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.CategoriaDao
import com.example.sticbrain.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    fun obtenerCategorias(): Flow<List<CategoriaEntity>> = categoriaDao.obtenerCategorias()

    fun obtenerCategoriasActivas(): Flow<List<CategoriaEntity>> = categoriaDao.obtenerCategoriasActivas()

    fun obtenerCategoriaPorId(id: Long): Flow<CategoriaEntity?> = categoriaDao.obtenerCategoriaPorId(id)

    fun buscarCategorias(query: String): Flow<List<CategoriaEntity>> = categoriaDao.buscarCategorias(query)

    suspend fun insertarCategoria(categoria: CategoriaEntity) {
        categoriaDao.insertar(categoria)
    }

    suspend fun actualizarCategoria(categoria: CategoriaEntity) {
        categoriaDao.actualizar(categoria)
    }

    suspend fun eliminarCategoria(categoria: CategoriaEntity) {
        categoriaDao.eliminar(categoria)
    }

    suspend fun contarCategorias(): Int = categoriaDao.contarCategorias()
}
