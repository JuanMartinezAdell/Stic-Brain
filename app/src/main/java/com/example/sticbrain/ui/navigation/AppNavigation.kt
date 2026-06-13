package com.example.sticbrain.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.sticbrain.ui.screens.HomeScreen

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
        composable(AppScreens.Incidencias.route) {
            Text(text = "Listado de Incidencias")
        }
        composable(
            route = AppScreens.IncidenciaDetalle.route,
            arguments = listOf(navArgument("incidenciaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("incidenciaId")
            Text(text = "Detalle de Incidencia ID: $id")
        }
        composable(AppScreens.IncidenciaCrear.route) {
            Text(text = "Crear Nueva Incidencia")
        }
        composable(
            route = AppScreens.IncidenciaEditar.route,
            arguments = listOf(navArgument("incidenciaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("incidenciaId")
            Text(text = "Editar Incidencia ID: $id")
        }
        composable(AppScreens.Busqueda.route) {
            Text(text = "Búsqueda de Incidencias")
        }
        composable(AppScreens.Categorias.route) {
            Text(text = "Gestión de Categorías")
        }
        composable(AppScreens.Aplicaciones.route) {
            Text(text = "Gestión de Aplicaciones")
        }
        composable(AppScreens.Entornos.route) {
            Text(text = "Gestión de Entornos")
        }
        composable(AppScreens.Proveedores.route) {
            Text(text = "Listado de Proveedores")
        }
        composable(AppScreens.ProveedorCrear.route) {
            Text(text = "Crear Nuevo Proveedor")
        }
        composable(
            route = AppScreens.ProveedorEditar.route,
            arguments = listOf(navArgument("proveedorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("proveedorId")
            Text(text = "Editar Proveedor ID: $id")
        }
    }
}
