package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.sticbrain.ui.components.BottomNavigationBar
import com.example.sticbrain.ui.components.IncidentCard
import com.example.sticbrain.ui.theme.SticBrainTheme

@Composable
fun SearchIncidentScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToIncidentDetail: (Long) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // Datos temporales para maquetación
    val allIncidents = remember {
        listOf(
            IncidentTemp(
                id = 1L,
                severity = "CRÍTICA",
                severityColor = Color(0xFFF85149),
                title = "Usuarios no pueden acceder a Soaris tras actualización",
                tag = "HIS",
                tagColor = Color(0xFFA371F7),
                subtitle = "Soaris HIS",
                status = "Resuelta",
                date = "2024-11-15"
            ),
            IncidentTemp(
                id = 2L,
                severity = "ALTA",
                severityColor = Color(0xFFDB6D28),
                title = "PACS sin acceso a imágenes desde Urgencias",
                tag = "PACS",
                tagColor = Color(0xFF388BFD),
                subtitle = "Sectra PACS",
                status = "Resuelta",
                date = "2024-11-08"
            ),
            IncidentTemp(
                id = 3L,
                severity = "MEDIA",
                severityColor = Color(0xFFD29922),
                title = "VPN Pulse Secure desconecta a los 5 minutos",
                tag = "VPN",
                tagColor = Color(0xFF3FB950),
                subtitle = "FortiGate SSL-VPN",
                status = "Resuelta",
                date = "2024-10-22"
            ),
            IncidentTemp(
                id = 4L,
                severity = "ALTA",
                severityColor = Color(0xFFDB6D28),
                title = "Impresoras de planta no imprimen etiquetas de paciente",
                tag = "Impresoras",
                tagColor = Color(0xFFDB6D28),
                subtitle = "Zebra ZD421 / Admisión HIS",
                status = "Resuelta",
                date = "2024-10-15"
            ),
            IncidentTemp(
                id = 5L,
                severity = "MEDIA",
                severityColor = Color(0xFFD29922),
                title = "Outlook no sincroniza correo tras migración de buzón",
                tag = "Correo",
                tagColor = Color(0xFFD29922),
                subtitle = "Microsoft Exchange / Outlook 365",
                status = "Resuelta",
                date = "2024-10-05"
            )
        )
    }

    // Búsqueda local temporal
    val filteredIncidents = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allIncidents
        } else {
            allIncidents.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.tag.contains(searchQuery, ignoreCase = true) ||
                it.subtitle.contains(searchQuery, ignoreCase = true) ||
                it.severity.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF0D1117),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 1, // Buscar is index 1
                onHomeClick = onNavigateToHome,
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
            // Barra de búsqueda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Buscar título, error, causa, solución...",
                            color = Color(0xFF8B949E),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF8B949E)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF161B22),
                        unfocusedContainerColor = Color(0xFF161B22),
                        disabledContainerColor = Color(0xFF161B22),
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    modifier = Modifier.size(56.dp),
                    color = Color(0xFF161B22),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    IconButton(onClick = { /* Filtro */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = Color(0xFF8B949E)
                        )
                    }
                }
            }

            // Contador de resultados
            Text(
                text = "${filteredIncidents.size} incidencias en la base de conocimientos",
                color = Color(0xFF8B949E),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Lista de incidencias
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredIncidents) { incident ->
                    IncidentCard(
                        severity = incident.severity,
                        severityColor = incident.severityColor,
                        title = incident.title,
                        tag = incident.tag,
                        tagColor = incident.tagColor,
                        subtitle = incident.subtitle,
                        status = incident.status,
                        date = incident.date,
                        onClick = { onNavigateToIncidentDetail(incident.id) }
                    )
                }
            }
        }
    }
}

// Modelo temporal
data class IncidentTemp(
    val id: Long,
    val severity: String,
    val severityColor: Color,
    val title: String,
    val tag: String,
    val tagColor: Color,
    val subtitle: String,
    val status: String,
    val date: String
)

@Preview(showBackground = true)
@Composable
fun SearchIncidentScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        SearchIncidentScreen()
    }
}
