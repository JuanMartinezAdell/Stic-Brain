package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.data.repository.IncidenciaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class IncidenciaViewModel(private val repository: IncidenciaRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Flow que combina todas las incidencias o las filtradas por búsqueda
    val incidencias: StateFlow<List<IncidenciaEntity>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAll()
            } else {
                repository.search(query)
            }
        }
        .onStart { _isLoading.value = true }
        .onEach { _isLoading.value = false }
        .catch { e ->
            _errorMessage.value = e.message
            _isLoading.value = false
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun insert(incidencia: IncidenciaEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.insert(incidencia)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun update(incidencia: IncidenciaEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.update(incidencia)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun delete(incidencia: IncidenciaEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.delete(incidencia)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
