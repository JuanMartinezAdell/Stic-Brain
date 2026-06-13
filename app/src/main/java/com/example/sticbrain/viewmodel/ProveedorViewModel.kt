package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.ProveedorEntity
import com.example.sticbrain.data.repository.ProveedorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProveedorViewModel(private val repository: ProveedorRepository) : ViewModel() {

    val proveedores: StateFlow<List<ProveedorEntity>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(proveedor: ProveedorEntity) {
        viewModelScope.launch {
            repository.insert(proveedor)
        }
    }

    fun update(proveedor: ProveedorEntity) {
        viewModelScope.launch {
            repository.update(proveedor)
        }
    }

    fun delete(proveedor: ProveedorEntity) {
        viewModelScope.launch {
            repository.delete(proveedor)
        }
    }
}
