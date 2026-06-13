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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.ui.theme.*

@Composable
fun HomeScreen(
    incidencias: List<IncidenciaEntity>,
    categorias: List<CategoriaEntity>,
    onNavigateToSearch: () -> Unit,
    onNavigateToNewIncident: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToIncidentDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 0,
                onHomeClick = {}, // Ya estamos en Home
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SticTopHeader(
                title = "Stic Brain",
                subtitle = "Base de conocimiento TIC · Hospital"
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                StatsSection(incidencias)
                FilterSection(categorias)
                IncidentsListSection(incidencias, onIncidentClick = onNavigateToIncidentDetail)
            }
        }
    }
}

@Composable
fun StatsSection(incidencias: List<IncidenciaEntity>, modifier: Modifier = Modifier) {
    val prioritariasCount = incidencias.count { it.nivelPrioridad.uppercase() == "CRÍTICA" || it.nivelPrioridad.uppercase() == "ALTA" }
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(count = "${incidencias.size}", label = "Entradas", color = SticBlue, modifier = Modifier.weight(1f))
        StatCard(count = "${incidencias.distinctBy { it.categoria }.size}", label = "Categorías", color = SticBlueLight, modifier = Modifier.weight(1f))
        StatCard(count = "$prioritariasCount", label = "Prioritarias", color = SticRed, modifier = Modifier.weight(1f))
        StatCard(count = "8", label = "Soporte", color = SticGreen, modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(count: String, label: String, color: Color, modifier: Modifier = Modifier) {
    SticCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = count, color = color, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = label, color = SticTextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
fun FilterSection(categorias: List<CategoriaEntity>, modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf("Todas") }

    LazyRow(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SticChip(
                text = "Todas",
                isSelected = selectedCategory == "Todas",
                onClick = { selectedCategory = "Todas" }
            )
        }
        items(categorias) { categoria ->
            SticChip(
                text = categoria.nombre,
                isSelected = selectedCategory == categoria.nombre,
                onClick = { selectedCategory = categoria.nombre }
            )
        }
    }
}

@Composable
fun IncidentsListSection(
    incidencias: List<IncidenciaEntity>,
    onIncidentClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Entradas recientes", color = SticTextPrimary, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = "Ver todas", color = SticBlue, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (incidencias.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(text = "No hay entradas disponibles", color = SticTextSecondary, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 1000.dp) // Limitar altura para evitar problemas con scroll anidado si procede
            ) {
                items(incidencias.take(5)) { incidencia ->
                    IncidentEntryCard(
                        category = incidencia.categoria,
                        title = incidencia.tituloNombre,
                        priority = incidencia.nivelPrioridad,
                        keywords = incidencia.palabrasClave.split(",").filter { it.isNotBlank() }.map { it.trim() },
                        procedure = incidencia.procedimientoRespuesta,
                        onClick = { onIncidentClick(incidencia.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun IncidentEntryCard(
    category: String,
    title: String,
    priority: String,
    keywords: List<String>,
    procedure: String,
    onClick: () -> Unit
) {
    SticCard(onClick = onClick) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = category, color = SticBlue, fontSize = 10.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            PriorityBadge(priority = priority)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(text = title, color = SticTextPrimary, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(text = procedure, color = SticTextSecondary, fontSize = 13.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
        
        if (keywords.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                keywords.forEach { keyword ->
                    Surface(color = SticSky, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = keyword,
                            color = SticBlue,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        incidencias = emptyList(),
        categorias = emptyList(),
        onNavigateToSearch = {},
        onNavigateToNewIncident = {},
        onNavigateToSupport = {},
        onNavigateToIncidentDetail = {}
    )
}
