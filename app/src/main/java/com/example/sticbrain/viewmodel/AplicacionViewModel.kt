package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.AplicacionEntity
import com.example.sticbrain.data.repository.AplicacionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AplicacionViewModel(private val repository: AplicacionRepository) : ViewModel() {

    val aplicaciones: StateFlow<List<AplicacionEntity>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(aplicacion: AplicacionEntity) {
        viewModelScope.launch {
            repository.insert(aplicacion)
        }
    }

    fun update(aplicacion: AplicacionEntity) {
        viewModelScope.launch {
            repository.update(aplicacion)
        }
    }

    fun delete(aplicacion: AplicacionEntity) {
        viewModelScope.launch {
            repository.delete(aplicacion)
        }
    }
}
