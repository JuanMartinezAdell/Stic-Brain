package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.ProveedorDao
import com.example.sticbrain.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar el directorio de proveedores de soporte.
 */
class ProveedorRepository(private val proveedorDao: ProveedorDao) {

    /** Obtiene el flujo de todos los proveedores registrados. */
    fun obtenerProveedores(): Flow<List<ProveedorEntity>> = proveedorDao.obtenerProveedores()

    /** Obtiene los datos de un proveedor específico. */
    fun obtenerProveedorPorId(id: Long): Flow<ProveedorEntity?> = proveedorDao.obtenerProveedorPorId(id)

    /** Busca proveedores por nombre, servicio o área técnica. */
    fun buscarProveedores(query: String): Flow<List<ProveedorEntity>> = proveedorDao.buscarProveedores(query)

    /** Añade un nuevo proveedor al directorio. */
    suspend fun insertarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.insertar(proveedor)
    }

    /** Actualiza los datos de un proveedor existente. */
    suspend fun actualizarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.actualizar(proveedor)
    }

    /** Borra un proveedor del directorio. */
    suspend fun eliminarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.eliminar(proveedor)
    }
}
