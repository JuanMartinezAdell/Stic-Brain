package com.example.sticbrain.ui.navigation

sealed class AppScreens(val route: String) {
    object Home : AppScreens("home")
    object Incidencias : AppScreens("incidencias")
    object IncidenciaDetalle : AppScreens("incidencia_detalle/{incidenciaId}") {
        fun createRoute(incidenciaId: Long) = "incidencia_detalle/$incidenciaId"
    }
    object IncidenciaCrear : AppScreens("incidencia_crear")
    object IncidenciaEditar : AppScreens("incidencia_editar/{incidenciaId}") {
        fun createRoute(incidenciaId: Long) = "incidencia_editar/$incidenciaId"
    }
    object Busqueda : AppScreens("busqueda")
    object Categorias : AppScreens("categorias")
    object Aplicaciones : AppScreens("aplicaciones")
    object Entornos : AppScreens("entornos")
    object Proveedores : AppScreens("proveedores")
    object ProveedorCrear : AppScreens("proveedor_crear")
    object ProveedorEditar : AppScreens("proveedor_editar/{proveedorId}") {
        fun createRoute(proveedorId: Long) = "proveedor_editar/$proveedorId"
    }
}
