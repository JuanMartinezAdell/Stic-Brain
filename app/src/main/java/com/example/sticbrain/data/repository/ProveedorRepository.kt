package com.example.sticbrain.data.repository

import com.example.sticbrain.data.local.dao.ProveedorDao
import com.example.sticbrain.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

class ProveedorRepository(private val proveedorDao: ProveedorDao) {

    fun getAll(): Flow<List<ProveedorEntity>> = proveedorDao.getAll()

    suspend fun getById(id: Long): ProveedorEntity? = proveedorDao.getById(id)

    suspend fun insert(proveedor: ProveedorEntity) {
        proveedorDao.insert(proveedor)
    }

    suspend fun update(proveedor: ProveedorEntity) {
        proveedorDao.update(proveedor)
    }

    suspend fun delete(proveedor: ProveedorEntity) {
        proveedorDao.delete(proveedor)
    }
}
