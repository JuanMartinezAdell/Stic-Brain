package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoriaViewModel(private val repository: CategoriaRepository) : ViewModel() {

    private val _query = MutableStateFlow("")

    val categorias: StateFlow<List<CategoriaEntity>> = repository.obtenerCategorias()
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
        _query.value = query
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
