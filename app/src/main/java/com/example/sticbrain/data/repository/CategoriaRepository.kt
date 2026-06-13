package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.CategoriaDao
import com.example.sticbrain.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    fun getAll(): Flow<List<CategoriaEntity>> = categoriaDao.getAll()

    suspend fun getById(id: Long): CategoriaEntity? = categoriaDao.getById(id)

    suspend fun insert(categoria: CategoriaEntity) {
        categoriaDao.insert(categoria)
    }

    suspend fun update(categoria: CategoriaEntity) {
        categoriaDao.update(categoria)
    }

    suspend fun delete(categoria: CategoriaEntity) {
        categoriaDao.delete(categoria)
    }
}
