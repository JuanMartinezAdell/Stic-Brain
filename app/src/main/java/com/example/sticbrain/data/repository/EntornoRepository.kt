package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.EntornoDao
import com.example.sticbrain.data.local.entity.EntornoEntity
import kotlinx.coroutines.flow.Flow

class EntornoRepository(private val entornoDao: EntornoDao) {

    fun getAll(): Flow<List<EntornoEntity>> = entornoDao.getAll()

    suspend fun getById(id: Long): EntornoEntity? = entornoDao.getById(id)

    suspend fun insert(entorno: EntornoEntity) {
        entornoDao.insert(entorno)
    }

    suspend fun update(entorno: EntornoEntity) {
        entornoDao.update(entorno)
    }

    suspend fun delete(entorno: EntornoEntity) {
        entornoDao.delete(entorno)
    }
}
