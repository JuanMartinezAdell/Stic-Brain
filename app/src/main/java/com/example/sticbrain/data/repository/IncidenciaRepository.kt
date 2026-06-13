package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.IncidenciaDao
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import kotlinx.coroutines.flow.Flow

class IncidenciaRepository(private val incidenciaDao: IncidenciaDao) {

    fun obtenerIncidencias(): Flow<List<IncidenciaEntity>> = incidenciaDao.obtenerIncidencias()

    fun obtenerIncidenciaPorId(id: Long): Flow<IncidenciaEntity?> = incidenciaDao.obtenerIncidenciaPorId(id)

    fun buscarIncidencias(query: String): Flow<List<IncidenciaEntity>> = incidenciaDao.buscarIncidencias(query)

    fun filtrarPorCategoria(categoria: String): Flow<List<IncidenciaEntity>> =
        incidenciaDao.filtrarPorCategoria(categoria)

    fun filtrarPorPrioridad(prioridad: String): Flow<List<IncidenciaEntity>> =
        incidenciaDao.filtrarPorPrioridad(prioridad)

    suspend fun insertarIncidencia(incidencia: IncidenciaEntity) {
        incidenciaDao.insertar(incidencia)
    }

    suspend fun insertarIncidencias(incidencias: List<IncidenciaEntity>) {
        incidenciaDao.insertarIncidencias(incidencias)
    }

    suspend fun actualizarIncidencia(incidencia: IncidenciaEntity) {
        incidenciaDao.actualizar(incidencia)
    }

    suspend fun eliminarIncidencia(incidencia: IncidenciaEntity) {
        incidenciaDao.eliminar(incidencia)
    }
}
