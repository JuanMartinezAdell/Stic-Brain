package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.IncidenciaDao
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import kotlinx.coroutines.flow.Flow

class IncidenciaRepository(private val incidenciaDao: IncidenciaDao) {

    fun getAll(): Flow<List<IncidenciaEntity>> = incidenciaDao.getAll()

    suspend fun getById(id: Long): IncidenciaEntity? = incidenciaDao.getById(id)

    suspend fun insert(incidencia: IncidenciaEntity) {
        incidenciaDao.insert(incidencia)
    }

    suspend fun update(incidencia: IncidenciaEntity) {
        incidenciaDao.update(incidencia)
    }

    suspend fun delete(incidencia: IncidenciaEntity) {
        incidenciaDao.delete(incidencia)
    }

    fun search(query: String): Flow<List<IncidenciaEntity>> = incidenciaDao.search(query)

    fun getByCategoria(categoriaId: Long): Flow<List<IncidenciaEntity>> =
        incidenciaDao.getByCategoria(categoriaId)

    fun getByAplicacion(aplicacionId: Long): Flow<List<IncidenciaEntity>> =
        incidenciaDao.getByAplicacion(aplicacionId)
}
