package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidenciaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(incidencia: IncidenciaEntity)

    @Update
    suspend fun actualizar(incidencia: IncidenciaEntity)

    @Delete
    suspend fun eliminar(incidencia: IncidenciaEntity)

    @Query("SELECT * FROM incidencias WHERE id = :id")
    fun obtenerIncidenciaPorId(id: Long): Flow<IncidenciaEntity?>

    @Query("SELECT * FROM incidencias ORDER BY fechaCreacion DESC")
    fun obtenerIncidencias(): Flow<List<IncidenciaEntity>>

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

    @Query("SELECT * FROM incidencias WHERE categoria = :categoria ORDER BY fechaCreacion DESC")
    fun filtrarPorCategoria(categoria: String): Flow<List<IncidenciaEntity>>

    @Query("SELECT * FROM incidencias WHERE nivelPrioridad = :prioridad ORDER BY fechaCreacion DESC")
    fun filtrarPorPrioridad(prioridad: String): Flow<List<IncidenciaEntity>>
}
