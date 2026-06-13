package com.example.sticbrain.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.sticbrain.data.local.database.SticBrainDatabase
import com.example.sticbrain.data.repository.CategoriaRepository
import com.example.sticbrain.data.repository.IncidenciaRepository
import com.example.sticbrain.data.repository.ProveedorRepository
import com.example.sticbrain.ui.screens.*
import com.example.sticbrain.viewmodel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Inicialización manual simple de Room y ViewModels
    val database = SticBrainDatabase.getDatabase(context)
    
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

    // Carga inicial de datos de prueba
    proveedorViewModel.cargarDatosPrueba()
    incidenciaViewModel.cargarDatosPrueba()
    categoriaViewModel.cargarCategoriasInicialesSiNecesario()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
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
                onNavigateToIncidentDetail = { id ->
                    navController.navigate(AppScreens.IncidenciaDetalle.createRoute(id))
                }
            )
        }

        composable(AppScreens.Ajustes.route) {
            SettingsScreen(
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
                onClearDemoData = {
                    // Acción temporal
                }
            )
        }

        composable(AppScreens.Categorias.route) {
            val categorias by categoriaViewModel.categorias.collectAsState()
            val queryCategoria by categoriaViewModel.queryBusqueda.collectAsState()

            CategoryManagementScreen(
                categorias = categorias,
                queryBusqueda = queryCategoria,
                onQueryChange = { query ->
                    categoriaViewModel.buscarCategorias(query)
                },
                onCreateCategory = { categoria ->
                    categoriaViewModel.insertarCategoria(categoria)
                },
                onUpdateCategory = { categoria ->
                    categoriaViewModel.actualizarCategoria(categoria)
                },
                onDeleteCategory = { categoria ->
                    categoriaViewModel.eliminarCategoria(categoria)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

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
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route)
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true }
                },
                onNavigateToIncidentDetail = { id ->
                    navController.navigate(AppScreens.IncidenciaDetalle.createRoute(id))
                }
            )
        }

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
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true }
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true }
                },
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
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true }
                },
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route)
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true }
                }
            )
        }

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
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true }
                },
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route)
                },
                onNavigateToProviderDetail = { id ->
                    navController.navigate(AppScreens.ProveedorDetalle.createRoute(id))
                }
            )
        }

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
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true }
                },
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true }
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true }
                }
            )
        }

        composable(
            route = AppScreens.ProveedorDetalle.route,
            arguments = listOf(navArgument("proveedorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("proveedorId") ?: 0L
            val proveedor by proveedorViewModel.obtenerProveedorPorId(id).collectAsState(initial = null)
            
            ProviderDetailScreen(
                proveedorId = id,
                proveedor = proveedor,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEditProvider = { proveedorId ->
                    navController.navigate(AppScreens.ProveedorEditar.createRoute(proveedorId))
                },
                onNavigateToNewProvider = {
                    navController.navigate(AppScreens.ProveedorCrear.route)
                },
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
                }
            )
        }

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
                onNavigateToSearch = {
                    navController.navigate(AppScreens.Busqueda.route) { launchSingleTop = true }
                },
                onNavigateToNewIncident = {
                    navController.navigate(AppScreens.IncidenciaCrear.route) { launchSingleTop = true }
                },
                onNavigateToSupport = {
                    navController.navigate(AppScreens.Proveedores.route) { launchSingleTop = true }
                }
            )
        }
    }
}
