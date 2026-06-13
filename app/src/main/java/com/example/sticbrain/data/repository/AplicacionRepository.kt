package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.AplicacionDao
import com.example.sticbrain.data.local.entity.AplicacionEntity
import kotlinx.coroutines.flow.Flow

class AplicacionRepository(private val aplicacionDao: AplicacionDao) {

    fun getAll(): Flow<List<AplicacionEntity>> = aplicacionDao.getAll()

    suspend fun getById(id: Long): AplicacionEntity? = aplicacionDao.getById(id)

    suspend fun insert(aplicacion: AplicacionEntity) {
        aplicacionDao.insert(aplicacion)
    }

    suspend fun update(aplicacion: AplicacionEntity) {
        aplicacionDao.update(aplicacion)
    }

    suspend fun delete(aplicacion: AplicacionEntity) {
        aplicacionDao.delete(aplicacion)
    }
}
