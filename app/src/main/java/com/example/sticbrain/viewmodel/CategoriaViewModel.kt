package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.repository.CategoriaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar el catálogo de categorías.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CategoriaViewModel(private val repository: CategoriaRepository) : ViewModel() {

    private val _queryBusqueda = MutableStateFlow("")
    /** Texto actual para filtrar categorías en la pantalla de gestión. */
    val queryBusqueda: StateFlow<String> = _queryBusqueda.asStateFlow()

    /** 
     * Lista completa de categorías, filtrada opcionalmente por nombre.
     */
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

    /** 
     * Lista de categorías marcadas como activas para su uso en formularios.
     */
    val categoriasActivas: StateFlow<List<CategoriaEntity>> = repository.obtenerCategoriasActivas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /** Recupera una categoría por ID. */
    fun obtenerCategoriaPorId(id: Long): Flow<CategoriaEntity?> {
        return repository.obtenerCategoriaPorId(id)
    }

    /** Actualiza el filtro de búsqueda de categorías. */
    fun buscarCategorias(query: String) {
        _queryBusqueda.value = query
    }

    /** Crea una nueva categoría. */
    fun insertarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.insertarCategoria(categoria)
        }
    }

    /** Modifica los datos de una categoría. */
    fun actualizarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.actualizarCategoria(categoria)
        }
    }

    /** Elimina una categoría. */
    fun eliminarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.eliminarCategoria(categoria)
        }
    }

    /**
     * Carga las categorías base de la plantilla Excel si la base de datos está vacía.
     */
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
