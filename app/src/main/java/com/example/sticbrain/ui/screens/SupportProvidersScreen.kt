package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.local.entity.ProveedorEntity
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla que muestra el directorio de proveedores de soporte y servicios técnicos.
 * 
 * Permite filtrar por área técnica y acceder a los datos de contacto y SLA
 * de cada servicio externo o interno.
 */
@Composable
fun SupportProvidersScreen(
    proveedores: List<ProveedorEntity>,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToProviderDetail: (Long) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Red", "HIS", "PACS", "Impresoras", "Correo")

    // Filtrado local según el chip seleccionado
    val filteredProviders = remember(selectedFilter, proveedores) {
        if (selectedFilter == "Todos") proveedores
        else proveedores.filter { it.categoriasRelacionadas?.contains(selectedFilter, ignoreCase = true) == true }
    }

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 3, // Soporte seleccionado
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Cabecera principal del módulo
            SticTopHeader(title = "Soporte y escalado", subtitle = "Directorio de proveedores TIC")
            
            Column(modifier = Modifier.padding(16.dp)) {
                // Selector de filtros rápidos
                LazyRow(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        SticChip(
                            text = filter,
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }

                // Listado de proveedores
                if (filteredProviders.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No se encontraron proveedores registrados", color = SticTextSecondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredProviders) { provider ->
                            SticCard(onClick = { onNavigateToProviderDetail(provider.id) }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Icono representativo
                                    Surface(color = SticSky, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Business, contentDescription = null, tint = SticBlue, modifier = Modifier.size(24.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = provider.nombre, color = SticTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        Text(text = provider.servicioAsociado, color = SticTextSecondary, fontSize = 13.sp)
                                    }
                                    Icon(Icons.Default.ChevronRight, contentDescription = "Ver detalle", tint = SticBorder)
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Información de contacto y SLA rápida
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Phone, null, tint = SticBlue, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = provider.telefono ?: "-", color = SticBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(text = "SLA: ${provider.sla ?: "-"}", color = SticGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupportProvidersScreenPreview() {
    SupportProvidersScreen(proveedores = emptyList())
}
