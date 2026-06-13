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
import com.example.sticbrain.ui.components.BottomNavigationBar
import com.example.sticbrain.ui.theme.SticBrainTheme

@Composable
fun SupportProvidersScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToProviderDetail: (Long) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Red", "HIS", "PACS", "Impresoras", "Correo")

    // Datos temporales para maquetación
    val allProviders = remember {
        listOf(
            ProviderTemp(1L, "Siemens Healthineers", "HIS · Soaris", listOf("HIS"), "900 102 345", "L-V", "4h crítica"),
            ProviderTemp(2L, "Sectra", "PACS · RIS · Visor DICOM", listOf("PACS"), "91 234 56 78", "24/7", "2h crítica"),
            ProviderTemp(3L, "Fortinet TAC", "Firewall · VPN · Red", listOf("Red", "VPN"), "900 900 111", "24/7", "1h crítica"),
            ProviderTemp(4L, "Microsoft CSSP", "Office 365 · AD · Exchange", listOf("Correo", "AD/GPO"), "900 800 700", "24/7", "4h crítica"),
            ProviderTemp(5L, "Proveedor Impresoras", "Zebra · Etiquetas · Impresión", listOf("Impresoras"), "900 555 222", "L-V", "8h alta"),
            ProviderTemp(6L, "CAU Corporativo", "Soporte nivel 1 · Escalado interno", listOf("General"), "955 000 000", "L-V", "Según prioridad")
        )
    }

    val filteredProviders = remember(selectedFilter) {
        if (selectedFilter == "Todos") allProviders
        else allProviders.filter { it.categories.contains(selectedFilter) }
    }

    Scaffold(
        containerColor = Color(0xFF0D1117),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 3, // Soporte is index 3
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Cabecera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Proveedores de soporte",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = Color(0xFF21262D),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${filteredProviders.size}",
                        color = Color(0xFF58A6FF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Filtros
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    SupportFilterChip(
                        text = filter,
                        isSelected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de proveedores
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProviders) { provider ->
                    ProviderCard(
                        provider = provider,
                        onClick = { onNavigateToProviderDetail(provider.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SupportFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF5865F2) else Color(0xFF161B22)
    val textColor = if (isSelected) Color.White else getSupportCategoryColor(text)

    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ProviderCard(
    provider: ProviderTemp,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color(0xFF161B22),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF21262D),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = Color(0xFF8B949E),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = provider.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = provider.service,
                            color = Color(0xFF8B949E),
                            fontSize = 13.sp
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF30363D),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = provider.availability,
                        color = Color(0xFF8B949E),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Categorías
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                provider.categories.forEach { category ->
                    val catColor = getSupportCategoryColor(category)
                    Surface(
                        color = catColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = category,
                            color = catColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhoneInTalk,
                        contentDescription = null,
                        tint = Color(0xFF8B949E),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = provider.phone,
                        color = Color(0xFF8B949E),
                        fontSize = 13.sp
                    )
                }
                Text(
                    text = "SLA: ${provider.sla}",
                    color = Color(0xFF3FB950),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
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

private fun getSupportCategoryColor(category: String): Color {
    return when (category) {
        "Red" -> Color(0xFF58A6FF)
        "HIS" -> Color(0xFFA371F7)
        "PACS" -> Color(0xFF388BFD)
        "Impresoras" -> Color(0xFFDB6D28)
        "Correo" -> Color(0xFFD29922)
        "VPN" -> Color(0xFF3FB950)
        "General" -> Color(0xFF8B949E)
        "AD/GPO" -> Color(0xFFF06292)
        else -> Color.White
    }
}

@Preview(showBackground = true)
@Composable
fun SupportProvidersScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        SupportProvidersScreen()
    }
}
