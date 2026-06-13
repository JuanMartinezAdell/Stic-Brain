package com.example.sticbrain.data.local.dao

import androidx.room.*
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidenciaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incidencia: IncidenciaEntity)

    @Update
    suspend fun update(incidencia: IncidenciaEntity)

    @Delete
    suspend fun delete(incidencia: IncidenciaEntity)

    @Query("SELECT * FROM incidencias WHERE id = :id")
    suspend fun getById(id: Long): IncidenciaEntity?

    @Query("SELECT * FROM incidencias ORDER BY fechaModificacion DESC")
    fun getAll(): Flow<List<IncidenciaEntity>>

    @Query("""
        SELECT * FROM incidencias 
        WHERE titulo LIKE '%' || :query || '%' 
        OR descripcion LIKE '%' || :query || '%' 
        OR mensajeError LIKE '%' || :query || '%' 
        OR causa LIKE '%' || :query || '%' 
        OR solucion LIKE '%' || :query || '%'
        ORDER BY fechaModificacion DESC
    """)
    fun search(query: String): Flow<List<IncidenciaEntity>>

    @Query("SELECT * FROM incidencias WHERE categoriaId = :categoriaId")
    fun getByCategoria(categoriaId: Long): Flow<List<IncidenciaEntity>>

    @Query("SELECT * FROM incidencias WHERE aplicacionId = :aplicacionId")
    fun getByAplicacion(aplicacionId: Long): Flow<List<IncidenciaEntity>>
}
