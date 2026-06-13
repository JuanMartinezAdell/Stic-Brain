package com.example.sticbrain.ui.navigation

sealed class AppScreens(val route: String) {
    object Home : AppScreens("home")
    object Busqueda : AppScreens("busqueda")
    object IncidenciaCrear : AppScreens("incidencia_crear")
    object IncidenciaDetalle : AppScreens("incidencia_detalle/{incidenciaId}") {
        fun createRoute(incidenciaId: Long) = "incidencia_detalle/$incidenciaId"
    }
    object Proveedores : AppScreens("proveedores")
    object ProveedorDetalle : AppScreens("proveedor_detalle/{proveedorId}") {
        fun createRoute(proveedorId: Long) = "proveedor_detalle/$proveedorId"
    }
    object ProveedorCrear : AppScreens("proveedor_crear")
    object ProveedorEditar : AppScreens("proveedor_editar/{proveedorId}") {
        fun createRoute(proveedorId: Long) = "proveedor_editar/$proveedorId"
    }
    object Categorias : AppScreens("categorias")
    object Ajustes : AppScreens("ajustes")
}
