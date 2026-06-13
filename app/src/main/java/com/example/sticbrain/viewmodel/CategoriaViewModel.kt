package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.repository.CategoriaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriaViewModel(private val repository: CategoriaRepository) : ViewModel() {

    private val _queryBusqueda = MutableStateFlow("")
    val queryBusqueda: StateFlow<String> = _queryBusqueda.asStateFlow()

    val categorias: StateFlow<List<CategoriaEntity>> = _queryBusqueda
        .flatMapLatest { q ->
            if (q.isBlank()) {
                repository.obtenerCategorias()
            } else {
                repository.buscarCategorias(q)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categoriasActivas: StateFlow<List<CategoriaEntity>> = repository.obtenerCategoriasActivas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun obtenerCategoriaPorId(id: Long): Flow<CategoriaEntity?> {
        return repository.obtenerCategoriaPorId(id)
    }

    fun buscarCategorias(query: String) {
        _queryBusqueda.value = query
    }

    fun insertarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.insertarCategoria(categoria)
        }
    }

    fun actualizarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.actualizarCategoria(categoria)
        }
    }

    fun eliminarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.eliminarCategoria(categoria)
        }
    }

    fun cargarCategoriasInicialesSiNecesario() {
        viewModelScope.launch {
            if (repository.contarCategorias() == 0) {
                val categoriasIniciales = listOf(
                    "Acceso y autenticación",
                    "Hardware",
                    "Red y conectividad",
                    "Software",
                    "Correo electrónico",
                    "Seguridad",
                    "Sistemas operativos",
                    "Impresión y escaneo",
                    "Servicios y aplicaciones internas",
                    "Telefonía",
                    "Soporte remoto",
                    "Consultas generales y formación"
                )
                categoriasIniciales.forEach { nombre ->
                    repository.insertarCategoria(CategoriaEntity(nombre = nombre))
                }
            }
        }
    }
}
