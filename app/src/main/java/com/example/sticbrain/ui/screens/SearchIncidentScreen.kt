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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla de búsqueda de la base de conocimiento.
 * 
 * Permite realizar búsquedas por texto libre y filtrar por categorías técnicas
 * para localizar rápidamente una ficha de respuesta.
 */
@Composable
fun SearchIncidentScreen(
    incidencias: List<IncidenciaEntity>,
    categorias: List<CategoriaEntity>,
    queryBusqueda: String,
    onQueryChange: (String) -> Unit,
    onCategoriaSelected: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToIncidentDetail: (Long) -> Unit = {}
) {
    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 1,
                onHomeClick = onNavigateToHome,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Cabecera sencilla
            SticTopHeader(title = "Buscador", subtitle = "Base de conocimiento TIC")
            
            Column(modifier = Modifier.padding(16.dp)) {
                // Campo de texto para búsqueda libre
                OutlinedTextField(
                    value = queryBusqueda,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar por título, frase de usuario, procedimiento...", color = SticTextSecondary, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = SticBlue) },
                    trailingIcon = { if (queryBusqueda.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Default.Clear, null) } },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SticWhite,
                        unfocusedContainerColor = SticWhite,
                        focusedBorderColor = SticBlue,
                        unfocusedBorderColor = SticBorder
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filtro rápido por categorías en horizontal
                LazyRow(
                    modifier = Modifier.padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        SticChip(
                            text = "Todas",
                            isSelected = queryBusqueda.isBlank(), // Simplificado: si no hay búsqueda, marcar Todas
                            onClick = { onCategoriaSelected("Todas") }
                        )
                    }
                    items(categorias) { categoria ->
                        SticChip(
                            text = categoria.nombre,
                            onClick = { onCategoriaSelected(categoria.nombre) }
                        )
                    }
                }

                Text(
                    text = "${incidencias.size} Entradas en la base de conocimiento",
                    color = SticTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Listado de resultados
                if (incidencias.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No se encontraron resultados", color = SticTextSecondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(incidencias) { entry ->
                            IncidentEntryCard(
                                category = entry.categoria,
                                title = entry.tituloNombre,
                                priority = entry.nivelPrioridad,
                                keywords = entry.palabrasClave.split(",").filter { it.isNotBlank() }.map { it.trim() },
                                procedure = entry.procedimientoRespuesta,
                                isProvisional = entry.esProvisional,
                                onClick = { onNavigateToIncidentDetail(entry.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchIncidentScreenPreview() {
    SearchIncidentScreen(
        incidencias = emptyList(),
        categorias = emptyList(),
        queryBusqueda = "",
        onQueryChange = {}
    )
}
