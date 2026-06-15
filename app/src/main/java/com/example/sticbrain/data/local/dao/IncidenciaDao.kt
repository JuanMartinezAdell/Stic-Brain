package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para definir las operaciones de la tabla "incidencias" en Room.
 */
@Dao
interface IncidenciaDao {
    /** Inserta una nueva ficha de conocimiento. Si el ID ya existe, lo reemplaza. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(incidencia: IncidenciaEntity)

    /** Inserta una lista de fichas de golpe. Útil para importaciones masivas. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarIncidencias(incidencias: List<IncidenciaEntity>)

    /** Actualiza los datos de una ficha existente. */
    @Update
    suspend fun actualizar(incidencia: IncidenciaEntity)

    /** Elimina una ficha de la base de datos. */
    @Delete
    suspend fun eliminar(incidencia: IncidenciaEntity)

    /** Busca una ficha concreta usando su ID. */
    @Query("SELECT * FROM incidencias WHERE id = :id")
    fun obtenerIncidenciaPorId(id: Long): Flow<IncidenciaEntity?>

    /** Obtiene todas las fichas ordenadas por la fecha de creación más reciente. */
    @Query("SELECT * FROM incidencias ORDER BY fechaCreacion DESC")
    fun obtenerIncidencias(): Flow<List<IncidenciaEntity>>

    /** Obtiene todas las fichas sin usar Flow. Útil para la exportación a Excel. */
    @Query("SELECT * FROM incidencias ORDER BY fechaCreacion DESC")
    suspend fun obtenerIncidenciasUnaVez(): List<IncidenciaEntity>

    /**
     * Realiza una búsqueda avanzada en múltiples campos de la tabla.
     * Busca coincidencias parciales en título, descripción, frases de usuario, etc.
     */
    @Query("""
        SELECT * FROM incidencias
        WHERE tituloNombre LIKE '%' || :query || '%'
        OR descripcion LIKE '%' || :query || '%'
        OR frasesUsuario LIKE '%' || :query || '%'
        OR procedimientoRespuesta LIKE '%' || :query || '%'
        OR palabrasClave LIKE '%' || :query || '%'
        OR categoria LIKE '%' || :query || '%'
        OR nivelPrioridad LIKE '%' || :query || '%'
        ORDER BY fechaCreacion DESC
    """)
    fun buscarIncidencias(query: String): Flow<List<IncidenciaEntity>>

    /** Filtra las fichas que pertenecen a una categoría específica. */
    @Query("SELECT * FROM incidencias WHERE categoria = :categoria ORDER BY fechaCreacion DESC")
    fun filtrarPorCategoria(categoria: String): Flow<List<IncidenciaEntity>>

    /** Filtra las fichas según su importancia (Baja, Media, Alta, Crítica). */
    @Query("SELECT * FROM incidencias WHERE nivelPrioridad = :prioridad ORDER BY fechaCreacion DESC")
    fun filtrarPorPrioridad(prioridad: String): Flow<List<IncidenciaEntity>>
    @Query("SELECT * FROM incidencias WHERE id IN (:ids)")
    suspend fun obtenerIncidenciasPorIds(ids: List<Long>): List<IncidenciaEntity>
}
