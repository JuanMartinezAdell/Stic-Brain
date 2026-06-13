package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.EntornoEntity
import com.example.sticbrain.data.repository.EntornoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntornoViewModel(private val repository: EntornoRepository) : ViewModel() {

    val entornos: StateFlow<List<EntornoEntity>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(entorno: EntornoEntity) {
        viewModelScope.launch {
            repository.insert(entorno)
        }
    }

    fun update(entorno: EntornoEntity) {
        viewModelScope.launch {
            repository.update(entorno)
        }
    }

    fun delete(entorno: EntornoEntity) {
        viewModelScope.launch {
            repository.delete(entorno)
        }
    }
}
