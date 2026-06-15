package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.IncidenciaDao
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio Incidencias
 * 
 * Esta clase separa el ViewModel del DAO (acceso a Room). Su función es
 * centralizar el acceso a los datos de las fichas, proporcionando métodos
 * limpios para el resto de la aplicación.
 */
class IncidenciaRepository(private val incidenciaDao: IncidenciaDao) {

    /** Obtiene el flujo constante de fichas guardadas. */
    fun obtenerIncidencias(): Flow<List<IncidenciaEntity>> = incidenciaDao.obtenerIncidencias()

    /** Obtiene la lista actual de fichas una sola vez (suspendido). */
    suspend fun obtenerIncidenciasUnaVez(): List<IncidenciaEntity> = incidenciaDao.obtenerIncidenciasUnaVez()

    /** Busca los datos de una ficha concreta por su identificador. */
    fun obtenerIncidenciaPorId(id: Long): Flow<IncidenciaEntity?> = incidenciaDao.obtenerIncidenciaPorId(id)

    /** Busca fichas que coincidan con un texto de búsqueda libre. */
    fun buscarIncidencias(query: String): Flow<List<IncidenciaEntity>> = incidenciaDao.buscarIncidencias(query)

    /** Filtra las fichas por su área técnica. */
    fun filtrarPorCategoria(categoria: String): Flow<List<IncidenciaEntity>> =
        incidenciaDao.filtrarPorCategoria(categoria)

    /** Filtra las fichas por su nivel de importancia. */
    fun filtrarPorPrioridad(prioridad: String): Flow<List<IncidenciaEntity>> =
        incidenciaDao.filtrarPorPrioridad(prioridad)

    /** Guarda una nueva ficha en Room. */
    suspend fun insertarIncidencia(incidencia: IncidenciaEntity) {
        incidenciaDao.insertar(incidencia)
    }

    /** Guarda varias fichas a la vez. */
    suspend fun insertarIncidencias(incidencias: List<IncidenciaEntity>) {
        incidenciaDao.insertarIncidencias(incidencias)
    }

    /** Actualiza los datos de una ficha ya guardada. */
    suspend fun actualizarIncidencia(incidencia: IncidenciaEntity) {
        incidenciaDao.actualizar(incidencia)
    }

    /** Borra permanentemente una ficha. */
    suspend fun eliminarIncidencia(incidencia: IncidenciaEntity) {
        incidenciaDao.eliminar(incidencia)
    }

    /**
     * Obtiene una lista de incidencias filtrando por una lista de identificadores.
     */
    suspend fun obtenerIncidenciasPorIds(ids: List<Long>): List<IncidenciaEntity> {
        return incidenciaDao.obtenerIncidenciasPorIds(ids)
    }
}
