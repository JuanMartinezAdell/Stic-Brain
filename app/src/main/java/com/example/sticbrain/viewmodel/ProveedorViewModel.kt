package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.ProveedorEntity
import com.example.sticbrain.data.repository.ProveedorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el directorio de proveedores de soporte.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProveedorViewModel(private val repository: ProveedorRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    
    /** Lista de proveedores filtrada por nombre o servicio. */
    val proveedores: StateFlow<List<ProveedorEntity>> = _query
        .flatMapLatest { q ->
            if (q.isBlank()) {
                repository.obtenerProveedores()
            } else {
                repository.buscarProveedores(q)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /** Actualiza el filtro de búsqueda. */
    fun buscarProveedores(query: String) {
        _query.value = query
    }

    /** Obtiene los detalles de un proveedor mediante un flujo. */
    fun obtenerProveedorPorId(id: Long): Flow<ProveedorEntity?> {
        return repository.obtenerProveedorPorId(id)
    }

    /** Registra un nuevo proveedor. */
    fun insertarProveedor(proveedor: ProveedorEntity) {
        viewModelScope.launch {
            repository.insertarProveedor(proveedor)
        }
    }

    /** Modifica un proveedor existente. */
    fun actualizarProveedor(proveedor: ProveedorEntity) {
        viewModelScope.launch {
            repository.actualizarProveedor(proveedor)
        }
    }

    /** Borra un proveedor. */
    fun eliminarProveedor(proveedor: ProveedorEntity) {
        viewModelScope.launch {
            repository.eliminarProveedor(proveedor)
        }
    }

    /**
     * Carga proveedores de ejemplo si no hay ninguno registrado.
     */
    fun cargarDatosPrueba() {
        viewModelScope.launch {
            repository.obtenerProveedores().first().let { currentList ->
                if (currentList.isEmpty()) {
                    val datosPrueba = listOf(
                        ProveedorEntity(nombre = "Soporte CAU", servicioAsociado = "Soporte Nivel 1", telefono = "955 000 000", horario = "L-V", sla = "Según prioridad"),
                        ProveedorEntity(nombre = "Siemens Healthineers", servicioAsociado = "HIS · Soaris", telefono = "900 102 345", email = "soporte.his@siemens.com", horario = "L-V", sla = "4h crítica"),
                        ProveedorEntity(nombre = "Sectra", servicioAsociado = "PACS · RIS", telefono = "91 234 56 78", email = "pacs.support@sectra.com", horario = "24/7", sla = "2h crítica"),
                        ProveedorEntity(nombre = "Fortinet TAC", servicioAsociado = "Firewall · VPN", telefono = "900 900 111", web = "support.fortinet.com", horario = "24/7", sla = "1h crítica"),
                        ProveedorEntity(nombre = "Microsoft CSSP", servicioAsociado = "Office 365 · AD", telefono = "900 800 700", horario = "24/7", sla = "4h crítica"),
                        ProveedorEntity(nombre = "Proveedor Impresoras", servicioAsociado = "Zebra · Impresión", telefono = "900 555 222", horario = "L-V", sla = "8h alta")
                    )
                    datosPrueba.forEach { p -> repository.insertarProveedor(p) }
                }
            }
        }
    }
}
