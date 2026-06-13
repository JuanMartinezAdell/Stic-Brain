package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriaViewModel(private val repository: CategoriaRepository) : ViewModel() {

    val categorias: StateFlow<List<CategoriaEntity>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.insert(categoria)
        }
    }

    fun update(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.update(categoria)
        }
    }

    fun delete(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.delete(categoria)
        }
    }
}
