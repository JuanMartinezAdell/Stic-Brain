package com.example.sticbrain.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.data.importer.*
import com.example.sticbrain.data.repository.IncidenciaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ImportUiState(
    val isImporting: Boolean = false,
    val result: ExcelImportResult? = null,
    val errorMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class IncidenciaViewModel(private val repository: IncidenciaRepository) : ViewModel() {

    private val _queryBusqueda = MutableStateFlow("")
    val queryBusqueda: StateFlow<String> = _queryBusqueda.asStateFlow()

    private val _categoriaFiltro = MutableStateFlow<String?>(null)
    private val _prioridadFiltro = MutableStateFlow<String?>(null)

    private val _importUiState = MutableStateFlow(ImportUiState())
    val importUiState: StateFlow<ImportUiState> = _importUiState.asStateFlow()

    val incidencias: StateFlow<List<IncidenciaEntity>> = combine(
        _queryBusqueda,
        _categoriaFiltro,
        _prioridadFiltro
    ) { query, categoria, prioridad ->
        Triple(query, categoria, prioridad)
    }.flatMapLatest { (query, categoria, prioridad) ->
        when {
            query.isNotBlank() -> repository.buscarIncidencias(query)
            categoria != null -> repository.filtrarPorCategoria(categoria)
            prioridad != null -> repository.filtrarPorPrioridad(prioridad)
            else -> repository.obtenerIncidencias()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun buscarIncidencias(query: String) {
        _queryBusqueda.value = query
        _categoriaFiltro.value = null
        _prioridadFiltro.value = null
    }

    fun filtrarPorCategoria(categoria: String?) {
        _categoriaFiltro.value = if (categoria == "Todas") null else categoria
        _queryBusqueda.value = ""
        _prioridadFiltro.value = null
    }

    fun filtrarPorPrioridad(prioridad: String?) {
        _prioridadFiltro.value = prioridad
        _queryBusqueda.value = ""
        _categoriaFiltro.value = null
    }

    fun obtenerIncidenciaPorId(id: Long): Flow<IncidenciaEntity?> {
        return repository.obtenerIncidenciaPorId(id)
    }

    fun insertarIncidencia(incidencia: IncidenciaEntity) {
        viewModelScope.launch {
            repository.insertarIncidencia(incidencia)
        }
    }

    fun importarExcelDesdeUri(
        context: Context,
        uri: Uri,
        excelImporter: ExcelImporter
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _importUiState.value = ImportUiState(isImporting = true)
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val (rows, result) = inputStream.use { excelImporter.importFromInputStream(it) }
                    
                    if (rows.isNotEmpty()) {
                        val entities = rows.map { it.toIncidenciaEntity() }
                        repository.insertarIncidencias(entities)
                    }
                    
                    _importUiState.value = ImportUiState(isImporting = false, result = result)
                } else {
                    _importUiState.value = ImportUiState(isImporting = false, errorMessage = "No se pudo abrir el archivo")
                }
            } catch (e: Exception) {
                _importUiState.value = ImportUiState(isImporting = false, errorMessage = "Error: ${e.message}")
            }
        }
    }

    fun resetImportState() {
        _importUiState.value = ImportUiState()
    }

    fun actualizarIncidencia(incidencia: IncidenciaEntity) {
        viewModelScope.launch {
            repository.actualizarIncidencia(incidencia)
        }
    }

    fun eliminarIncidencia(incidencia: IncidenciaEntity) {
        viewModelScope.launch {
            repository.eliminarIncidencia(incidencia)
        }
    }

    fun cargarDatosPrueba() {
        viewModelScope.launch {
            val currentList = repository.obtenerIncidencias().first()
            if (currentList.isEmpty()) {
                val datosPrueba = listOf(
                    IncidenciaEntity(
                        categoria = "Acceso y autenticación",
                        tituloNombre = "Acceso a Mosaiq",
                        descripcion = "Usuario solicita el acceso a la aplicación Mosaiq",
                        frasesUsuario = "Acceso a Mosaiq",
                        procedimientoRespuesta = "Incluir el grupo SE00_GA_MosaiqRegUsuarios",
                        palabrasClave = "mosaiq, radiofisica",
                        nivelPrioridad = "Normal",
                        notasComentarios = "Sin observaciones"
                    ),
                    IncidenciaEntity(
                        categoria = "Acceso y autenticación",
                        tituloNombre = "Acceso carpetas compartidas NAS",
                        descripcion = "Usuario solicita acceso a carpeta compartida de la NAS",
                        frasesUsuario = "Solicito acceso a carpeta compartida",
                        procedimientoRespuesta = "El acceso a las carpetas compartidas del Servicio son gestionadas por el responsable o personas delegadas mediante la aplicación Sevilla NAS. Informática no proporciona permisos a dichas carpetas.",
                        palabrasClave = "NAS, carpeta compartida",
                        nivelPrioridad = "Normal",
                        notasComentarios = "Informática no proporciona permisos directamente."
                    ),
                    IncidenciaEntity(
                        categoria = "Acceso y autenticación",
                        tituloNombre = "Acceso a Omnicel",
                        descripcion = "Usuario solicita acceso a Omnicel",
                        frasesUsuario = "Solicito acceso a Omnicel",
                        procedimientoRespuesta = "Las credenciales de acceso a Omnicel son proporcionadas por el Supervisor del Servicio o por Farmacia.",
                        palabrasClave = "omnicel, armario dispensador, farmacia",
                        nivelPrioridad = "Normal",
                        notasComentarios = "Validar con responsable del servicio."
                    )
                )
                datosPrueba.forEach { repository.insertarIncidencia(it) }
            }
        }
    }
}
