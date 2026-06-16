package com.example.sticbrain.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.sticbrain.data.importer.ExcelImporterImpl
import com.example.sticbrain.data.exporter.ExcelExporterImpl
import com.example.sticbrain.data.auth.GoogleAccountManager
import com.example.sticbrain.data.local.database.SticBrainDatabase
import com.example.sticbrain.data.repository.CategoriaRepository
import com.example.sticbrain.data.repository.ChatbotConfigRepository
import com.example.sticbrain.data.repository.ChatMessageRepository
import com.example.sticbrain.data.repository.IncidenciaRepository
import com.example.sticbrain.data.repository.ProveedorRepository
import com.example.sticbrain.data.security.SecureApiKeyStorage
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.screens.*
import com.example.sticbrain.viewmodel.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Componente principal de navegación de la aplicación.
 * 
 * Aquí se definen todas las pantallas, sus rutas y la lógica de paso
 * de información entre ellas. También se inicializan los ViewModels
 * y se conectan con los repositorios y la base de datos Room.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // INICIALIZACIÓN DE LA CAPA DE DATOS (ROOM)
    // Se obtiene la instancia única de la base de datos.
    val database = SticBrainDatabase.getDatabase(context)
    
    // SERVICIOS AUXILIARES
    val excelImporter = ExcelImporterImpl()
    val excelExporter = ExcelExporterImpl()
    
    // REPOSITORIOS Y VIEWMODELS
    // El repositorio actúa como fuente de verdad para el ViewModel.
    val proveedorRepository = ProveedorRepository(database.proveedorDao())
    val proveedorViewModel: ProveedorViewModel = viewModel(
        factory = ProveedorViewModelFactory(proveedorRepository)
    )

    val incidenciaRepository = IncidenciaRepository(database.incidenciaDao())
    val incidenciaViewModel: IncidenciaViewModel = viewModel(
        factory = IncidenciaViewModelFactory(incidenciaRepository)
    )

    val categoriaRepository = CategoriaRepository(database.categoriaDao())
    val categoriaViewModel: CategoriaViewModel = viewModel(
        factory = CategoriaViewModelFactory(categoriaRepository)
    )

    val chatMessageRepository = ChatMessageRepository(database.chatMessageDao())
    val secureApiKeyStorage = SecureApiKeyStorage(context)
    val chatbotConfigRepository = ChatbotConfigRepository(
        chatbotConfigDao = database.chatbotConfigDao(),
        secureApiKeyStorage = secureApiKeyStorage
    )
    
    val chatbotViewModel: ChatbotViewModel = viewModel(
        factory = ChatbotViewModelFactory(incidenciaRepository, chatMessageRepository, chatbotConfigRepository)
    )
    
    val chatbotConfigViewModel: ChatbotConfigViewModel = viewModel(
        factory = ChatbotConfigViewModelFactory(chatbotConfigRepository)
    )

    // CARGA DE DATOS INICIALES
    // Si la base de datos está vacía, se insertan los ejemplos de la plantilla.
    LaunchedEffect(Unit) {
        proveedorViewModel.cargarDatosPrueba()
        incidenciaViewModel.cargarDatosPrueba()
        categoriaViewModel.cargarCategoriasInicialesSiNecesario()
    }

    // DEFINICIÓN DEL NAVHOST (Gestor de rutas)
    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
        
        // PANTALLA DE INICIO
        composable(AppScreens.Home.route) {
            val incidencias by incidenciaViewModel.incidencias.collectAsState()
            val categorias by categoriaViewModel.categoriasActivas.collectAsState()
            HomeScreen(
                incidencias = incidencias,
                categorias = categorias,
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) },
                onNavigateToCategories = { navController.navigate(AppScreens.Categorias.route) },
                onNavigateToSettings = { navController.navigate(AppScreens.Ajustes.route) },
                onNavigateToChatbot = { navController.navigate(AppScreens.Chatbot.route) },
                onNavigateToIncidentDetail = { id ->
                    navController.navigate(AppScreens.IncidenciaDetalle.createRoute(id))
                }
            )
        }

        // PANTALLA DE AJUSTES Y UTILIDADES
        composable(AppScreens.Ajustes.route) {
            val importUiState by incidenciaViewModel.importUiState.collectAsState()
            val exportUiState by incidenciaViewModel.exportUiState.collectAsState()
            SettingsScreen(
                importUiState = importUiState,
                exportUiState = exportUiState,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true }
                },
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true }
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true }
                },
                onNavigateToCategories = {
                    navController.navigate(AppScreens.Categorias.route)
                },
                onNavigateToProviders = {
                    navController.navigate(AppScreens.Proveedores.route)
                },
                onNavigateToChatbot = {
                    navController.navigate(AppScreens.Chatbot.route)
                },
                onImportExcelFile = { uri ->
                    incidenciaViewModel.importarExcelDesdeUri(context, uri, excelImporter)
                },
                onExportExcelFile = { uri ->
                    incidenciaViewModel.exportarExcelAUri(context, uri, excelExporter)
                },
                onResetImportState = { incidenciaViewModel.resetImportState() },
                onResetExportState = { incidenciaViewModel.resetExportState() },
                onNavigateToChatbotSettings = {
                    navController.navigate(AppScreens.ChatbotSettings.route)
                }
            )
        }

        composable(AppScreens.ChatbotSettings.route) {
            val configUiState by chatbotConfigViewModel.uiState.collectAsState()
            val scope = rememberCoroutineScope()
            val googleManager = GoogleAccountManager(context)

            ChatbotSettingsScreen(
                uiState = configUiState,
                onModeChange = chatbotConfigViewModel::onModeChange,
                onApiKeyChange = chatbotConfigViewModel::onApiKeyChange,
                onModelChange = chatbotConfigViewModel::onModelChange,
                onMaxContextIncidentsChange = chatbotConfigViewModel::onMaxContextIncidentsChange,
                onResponseStyleChange = chatbotConfigViewModel::onResponseStyleChange,
                onDetailLevelChange = chatbotConfigViewModel::onDetailLevelChange,
                onSaveConfig = chatbotConfigViewModel::guardarConfiguracion,
                onDeleteApiKey = chatbotConfigViewModel::eliminarApiKey,
                onConfirmGoogleAccount = {
                    scope.launch {
                        // El clientId se obtiene de la consola de Google Cloud (Web Client ID)
                        val result = googleManager.confirmarCuentaGoogle(
                            serverClientId = "TU_CLIENT_ID_DE_GOOGLE.apps.googleusercontent.com"
                        )
                        result?.let {
                            chatbotConfigViewModel.onGoogleAccountConfirmed(it.email, it.displayName)
                        }
                    }
                },
                onRemoveGoogleAccount = chatbotConfigViewModel::eliminarCuentaGoogle,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(AppScreens.Home.route) },
                onNavigateToChatbot = { navController.navigate(AppScreens.Chatbot.route) }
            )
        }

        composable(AppScreens.Chatbot.route) {
            val messages by chatbotViewModel.messages.collectAsState()
            val currentQuestion by chatbotViewModel.currentQuestion.collectAsState()
            val isLoading by chatbotViewModel.isLoading.collectAsState()
            val modeLabel by chatbotViewModel.currentModeLabel.collectAsState()

            ChatbotScreen(
                messages = messages,
                currentQuestion = currentQuestion,
                isLoading = isLoading,
                chatbotModeLabel = modeLabel,
                onQuestionChange = chatbotViewModel::onQuestionChange,
                onSendQuestion = chatbotViewModel::sendQuestion,
                onClearConversation = chatbotViewModel::clearConversation,
                onOpenIncidentDetail = { id ->
                    navController.navigate(AppScreens.IncidenciaDetalle.createRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = { navController.navigate(AppScreens.Home.route) },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) },
                onNavigateToSettings = { navController.navigate(AppScreens.Ajustes.route) }
            )
        }

        // PANTALLA DE GESTIÓN DE CATEGORÍAS
        composable(AppScreens.Categorias.route) {
            val categorias by categoriaViewModel.categorias.collectAsState()
            val queryCategoria by categoriaViewModel.queryBusqueda.collectAsState()

            CategoryManagementScreen(
                categorias = categorias,
                queryBusqueda = queryCategoria,
                onQueryChange = { query -> categoriaViewModel.buscarCategorias(query) },
                onCreateCategory = { cat -> categoriaViewModel.insertarCategoria(cat) },
                onUpdateCategory = { cat -> categoriaViewModel.actualizarCategoria(cat) },
                onDeleteCategory = { cat -> categoriaViewModel.eliminarCategoria(cat) },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true } },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } }
            )
        }

        // PANTALLA DE BÚSQUEDA AVANZADA
        composable(AppScreens.Busqueda.route) {
            val incidencias by incidenciaViewModel.incidencias.collectAsState()
            val categorias by categoriaViewModel.categoriasActivas.collectAsState()
            val query by incidenciaViewModel.queryBusqueda.collectAsState()
            SearchIncidentScreen(
                incidencias = incidencias,
                categorias = categorias,
                queryBusqueda = query,
                onQueryChange = { incidenciaViewModel.buscarIncidencias(it) },
                onCategoriaSelected = { incidenciaViewModel.filtrarPorCategoria(it) },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } },
                onNavigateToIncidentDetail = { id ->
                    navController.navigate(AppScreens.IncidenciaDetalle.createRoute(id))
                }
            )
        }

        // PANTALLA DE NUEVA ENTRADA (FICHA)
        composable(AppScreens.IncidenciaCrear.route) {
            val categorias by categoriaViewModel.categoriasActivas.collectAsState()
            NewIncidentScreen(
                categorias = categorias,
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } },
                onNavigateBack = { navController.popBackStack() },
                onSaveIncident = { incidencia ->
                    incidenciaViewModel.insertarIncidencia(incidencia)
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        // PANTALLA DE DETALLE DE FICHA
        composable(
            route = AppScreens.IncidenciaDetalle.route,
            arguments = listOf(navArgument("incidenciaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("incidenciaId") ?: 0L
            val incidencia by incidenciaViewModel.obtenerIncidenciaPorId(id).collectAsState(initial = null)
            
            IncidentDetailScreen(
                incidenciaId = id,
                incidencia = incidencia,
                onNavigateBack = { navController.popBackStack() },
                onDeleteIncident = {
                    incidencia?.let {
                        incidenciaViewModel.eliminarIncidencia(it)
                        navController.popBackStack()
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } }
            )
        }

        // PANTALLA DE DIRECTORIO DE SOPORTE
        composable(AppScreens.Proveedores.route) {
            val proveedores by proveedorViewModel.proveedores.collectAsState()
            SupportProvidersScreen(
                proveedores = proveedores,
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) },
                onNavigateToProviderDetail = { id ->
                    navController.navigate(AppScreens.ProveedorDetalle.createRoute(id))
                }
            )
        }

        // PANTALLA DE ALTA DE PROVEEDOR
        composable(AppScreens.ProveedorCrear.route) {
            ProviderFormScreen(
                isEditMode = false,
                onNavigateBack = { navController.popBackStack() },
                onSaveProvider = { proveedor ->
                    proveedorViewModel.insertarProveedor(proveedor)
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true } },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } }
            )
        }

        // PANTALLA DE DETALLE DE PROVEEDOR
        composable(
            route = AppScreens.ProveedorDetalle.route,
            arguments = listOf(navArgument("proveedorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("proveedorId") ?: 0L
            val proveedor by proveedorViewModel.obtenerProveedorPorId(id).collectAsState(initial = null)
            
            ProviderDetailScreen(
                proveedorId = id,
                proveedor = proveedor,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditProvider = { idProv -> navController.navigate(AppScreens.ProveedorEditar.createRoute(idProv)) },
                onNavigateToNewProvider = { navController.navigate(AppScreens.ProveedorCrear.route) },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true } },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } }
            )
        }

        // PANTALLA DE EDICIÓN DE PROVEEDOR
        composable(
            route = AppScreens.ProveedorEditar.route,
            arguments = listOf(navArgument("proveedorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("proveedorId") ?: 0L
            val proveedor by proveedorViewModel.obtenerProveedorPorId(id).collectAsState(initial = null)

            ProviderFormScreen(
                proveedorId = id,
                proveedor = proveedor,
                isEditMode = true,
                onNavigateBack = { navController.popBackStack() },
                onSaveProvider = { proveedorActualizado ->
                    proveedorViewModel.actualizarProveedor(proveedorActualizado)
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true } },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true } },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true } }
            )
        }
    }
}
