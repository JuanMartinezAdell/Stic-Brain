package com.example.sticbrain.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.sticbrain.ui.screens.HomeScreen
import com.example.sticbrain.ui.screens.IncidentDetailScreen
import com.example.sticbrain.ui.screens.NewIncidentScreen
import com.example.sticbrain.ui.screens.ProviderDetailScreen
import com.example.sticbrain.ui.screens.ProviderFormScreen
import com.example.sticbrain.ui.screens.SearchIncidentScreen
import com.example.sticbrain.ui.screens.SupportProvidersScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
        composable(AppScreens.Home.route) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(AppScreens.Busqueda.route) },
                onNavigateToNewIncident = { navController.navigate(AppScreens.IncidenciaCrear.route) },
                onNavigateToSupport = { navController.navigate(AppScreens.Proveedores.route) },
                onNavigateToIncidentDetail = { id ->
                    navController.navigate(AppScreens.IncidenciaDetalle.createRoute(id))
                }
            )
        }

        composable(AppScreens.Busqueda.route) {
            SearchIncidentScreen(
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
            NewIncidentScreen(
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
                onSaveIncident = {
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
            IncidentDetailScreen(
                incidenciaId = id,
                onNavigateBack = { navController.popBackStack() },
                onDeleteIncident = { /* Implementación futura */ },
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
            SupportProvidersScreen(
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
                onSaveProvider = { navController.popBackStack() },
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
            ProviderDetailScreen(
                proveedorId = id,
                onNavigateBack = { navController.popBackStack() },
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
            ProviderFormScreen(
                proveedorId = id,
                isEditMode = true,
                onNavigateBack = { navController.popBackStack() },
                onSaveProvider = { navController.popBackStack() },
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
