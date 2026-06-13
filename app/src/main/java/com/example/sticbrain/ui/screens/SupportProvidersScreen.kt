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
import com.example.sticbrain.ui.theme.*

@Composable
fun SupportProvidersScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToProviderDetail: (Long) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Red", "HIS", "PACS", "Impresoras", "Correo")

    // Datos temporales adaptados
    val allProviders = remember {
        listOf(
            ProviderTemp(1L, "Siemens Healthineers", "HIS · Soaris", listOf("HIS"), "900 102 345", "L-V", "4h crítica"),
            ProviderTemp(2L, "Sectra", "PACS · RIS · Visor DICOM", listOf("PACS"), "91 234 56 78", "24/7", "2h crítica"),
            ProviderTemp(3L, "Fortinet TAC", "Firewall · VPN · Red", listOf("Red", "VPN"), "900 900 111", "24/7", "1h crítica"),
            ProviderTemp(4L, "CAU Corporativo", "Soporte nivel 1", listOf("General"), "955 000 000", "L-V", "Según prioridad")
        )
    }

    val filteredProviders = remember(selectedFilter) {
        if (selectedFilter == "Todos") allProviders
        else allProviders.filter { it.categories.contains(selectedFilter) }
    }

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 3,
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            SticTopHeader(title = "Soporte y escalado", subtitle = "Directorio de proveedores TIC")
            
            Column(modifier = Modifier.padding(16.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filters) { filter ->
                        SticChip(
                            text = filter,
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredProviders) { provider ->
                        SticCard(onClick = { onNavigateToProviderDetail(provider.id) }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = SticSky, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Business, null, tint = SticBlue, modifier = Modifier.size(24.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = provider.name, color = SticTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(text = provider.service, color = SticTextSecondary, fontSize = 13.sp)
                                }
                                Icon(Icons.Default.ChevronRight, null, tint = SticBorder)
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, null, tint = SticBlue, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = provider.phone, color = SticBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(text = "SLA: ${provider.sla}", color = SticGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ProviderTemp(
    val id: Long,
    val name: String,
    val service: String,
    val categories: List<String>,
    val phone: String,
    val availability: String,
    val sla: String
)

@Preview(showBackground = true)
@Composable
fun SupportProvidersScreenPreview() {
    SupportProvidersScreen()
}
