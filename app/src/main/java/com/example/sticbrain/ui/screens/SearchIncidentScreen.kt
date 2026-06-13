package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.ui.theme.*

@Composable
fun SearchIncidentScreen(
    incidencias: List<IncidenciaEntity>,
    queryBusqueda: String,
    onQueryChange: (String) -> Unit,
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
            SticTopHeader(title = "Buscador", subtitle = "Base de conocimiento TIC")
            
            Column(modifier = Modifier.padding(16.dp)) {
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

                Text(
                    text = "${incidencias.size} Entradas en la base de conocimiento",
                    color = SticTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (incidencias.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No se encontraron resultados", color = SticTextSecondary)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(incidencias) { entry ->
                            IncidentEntryCard(
                                category = entry.categoria,
                                title = entry.tituloNombre,
                                priority = entry.nivelPrioridad,
                                keywords = entry.palabrasClave.split(",").filter { it.isNotBlank() }.map { it.trim() },
                                procedure = entry.procedimientoRespuesta,
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
        queryBusqueda = "",
        onQueryChange = {}
    )
}
