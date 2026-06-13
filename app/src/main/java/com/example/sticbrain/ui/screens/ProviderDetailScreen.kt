package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.ui.theme.*

@Composable
fun ProviderDetailScreen(
    proveedorId: Long,
    onNavigateBack: () -> Unit = {},
    onNavigateToEditProvider: (Long) -> Unit = {},
    onNavigateToNewProvider: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    // Datos temporales
    val providerName = "Siemens Healthineers"
    val serviceArea = "HIS · Soaris"
    val contactPhone = "900 102 345"
    val contactEmail = "soporte.his@siemens-healthineers.com"
    val contactWeb = "soporte.siemens.com"
    val schedule = "L-V 08:00-20:00"
    val sla = "4h crítica / 8h alta"
    val notes = "Escalar incidencias críticas cuando afecten a varios servicios asistenciales."

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 3,
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            SticTopHeader(
                title = "Datos de soporte",
                subtitle = "#P$proveedorId · Soporte externo",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = { onNavigateToEditProvider(proveedorId) }) {
                        Icon(Icons.Default.Edit, null, tint = SticWhite)
                    }
                }
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                SticCard {
                    Text(text = providerName, color = SticTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = serviceArea, color = SticBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                
                SectionTitle(text = "CONTACTO", icon = Icons.Default.ContactPhone)
                SticCard {
                    ContactRow(icon = Icons.Default.Phone, text = contactPhone)
                    ContactRow(icon = Icons.Default.Email, text = contactEmail)
                    ContactRow(icon = Icons.Default.Language, text = contactWeb)
                }
                
                SectionTitle(text = "HORARIO", icon = Icons.Default.Schedule)
                SticCard {
                    Text(text = schedule, color = SticTextPrimary, fontSize = 14.sp)
                }
                
                SectionTitle(text = "SLA", icon = Icons.Default.Timer)
                SticCard {
                    Text(text = sla, color = SticGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                
                SectionTitle(text = "NOTAS / COMENTARIOS", icon = Icons.Default.Notes)
                SticCard {
                    Text(text = notes, color = SticTextSecondary, fontSize = 13.sp)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onNavigateToNewProvider,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SticBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Nuevo proveedor", color = SticWhite, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, tint = SticBlue, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = SticTextPrimary, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ProviderDetailScreenPreview() {
    ProviderDetailScreen(proveedorId = 1L)
}
