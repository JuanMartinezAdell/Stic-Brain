package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.ProveedorDao
import com.example.sticbrain.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

class ProveedorRepository(private val proveedorDao: ProveedorDao) {

    fun obtenerProveedores(): Flow<List<ProveedorEntity>> = proveedorDao.obtenerProveedores()

    fun obtenerProveedorPorId(id: Long): Flow<ProveedorEntity?> = proveedorDao.obtenerProveedorPorId(id)

    fun buscarProveedores(query: String): Flow<List<ProveedorEntity>> = proveedorDao.buscarProveedores(query)

    suspend fun insertarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.insertar(proveedor)
    }

    suspend fun actualizarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.actualizar(proveedor)
    }

    suspend fun eliminarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.eliminar(proveedor)
    }
}
