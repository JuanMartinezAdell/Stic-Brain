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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.R
import com.example.sticbrain.ui.theme.*

@Composable
fun HomeScreen(
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
                StatsSection()
                FilterSection()
                IncidentsListSection(onIncidentClick = onNavigateToIncidentDetail)
            }
        }
    }
}

@Composable
fun StatsSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(count = "42", label = "Entradas", color = SticBlue, modifier = Modifier.weight(1f))
        StatCard(count = "12", label = "Categorías", color = SticBlueLight, modifier = Modifier.weight(1f))
        StatCard(count = "5", label = "Prioritarias", color = SticRed, modifier = Modifier.weight(1f))
        StatCard(count = "8", label = "Soporte", color = SticGreen, modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(count: String, label: String, color: Color, modifier: Modifier = Modifier) {
    SticCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = count, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = SticTextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
fun FilterSection(modifier: Modifier = Modifier) {
    val categories = listOf("Todas", "Acceso", "Hardware", "Red", "Software", "Correo", "Seguridad", "Impresión", "Soporte remoto")
    var selectedCategory by remember { mutableStateOf("Todas") }

    LazyRow(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            SticChip(
                text = category,
                isSelected = selectedCategory == category,
                onClick = { selectedCategory = category }
            )
        }
    }
}

@Composable
fun IncidentsListSection(
    onIncidentClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Entradas recientes", color = SticTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "Ver todas", color = SticBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                IncidentEntryCard(
                    category = "Acceso y autenticación",
                    title = "Acceso a Mosaiq",
                    priority = "Normal",
                    keywords = listOf("mosaiq", "radiofísica"),
                    procedure = "Incluir el grupo SE00_GA_MosaiqRegUsuarios",
                    onClick = { onIncidentClick(1L) }
                )
            }
            item {
                IncidentEntryCard(
                    category = "Acceso y autenticación",
                    title = "Acceso carpetas compartidas NAS",
                    priority = "Normal",
                    keywords = listOf("NAS", "carpeta compartida"),
                    procedure = "El acceso es gestionado por Sevilla NAS",
                    onClick = { onIncidentClick(2L) }
                )
            }
            item {
                IncidentEntryCard(
                    category = "Sistemas operativos",
                    title = "Bloqueo cuenta de usuario",
                    priority = "Alta",
                    keywords = listOf("AD", "bloqueo", "login"),
                    procedure = "Desbloquear en Active Directory Users and Computers",
                    onClick = { onIncidentClick(3L) }
                )
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
            Text(text = category, color = SticBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            PriorityBadge(priority = priority)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(text = title, color = SticTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(text = procedure, color = SticTextSecondary, fontSize = 13.sp, maxLines = 1)
        
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToSearch = {},
        onNavigateToNewIncident = {},
        onNavigateToSupport = {},
        onNavigateToIncidentDetail = {}
    )
}
