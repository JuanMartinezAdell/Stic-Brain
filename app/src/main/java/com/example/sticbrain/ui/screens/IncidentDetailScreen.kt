package com.example.sticbrain.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.ui.theme.*

@Composable
fun IncidentDetailScreen(
    incidenciaId: Long,
    onNavigateBack: () -> Unit = {},
    onDeleteIncident: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    // Datos temporales
    val category = "Acceso y autenticación"
    val title = "Acceso carpetas compartidas NAS"
    val description = "Usuario solicita acceso a carpeta compartida de la NAS"
    val userPhrases = "Solicito acceso a carpeta compartida"
    val procedure = "El acceso a las carpetas compartidas del Servicio son gestionadas por el responsable o personas delegadas mediante la aplicación Sevilla NAS."
    val keywords = listOf("NAS", "carpeta compartida", "permisos")
    val priority = "Normal"
    val notes = "Informática no proporciona permisos directamente."

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            SticTopHeader(
                title = "Ficha de conocimiento",
                subtitle = "#$incidenciaId · $category",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = onDeleteIncident) {
                        Icon(Icons.Default.Delete, null, tint = SticWhite)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = category, color = SticBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        PriorityBadge(priority = priority)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = title, color = SticTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                
                SectionTitle(text = "DESCRIPCIÓN", icon = Icons.Default.Description)
                SticCard {
                    Text(text = description, color = SticTextPrimary, fontSize = 14.sp)
                }
                
                SectionTitle(text = "FRASES DE USUARIO", icon = Icons.Default.ChatBubbleOutline)
                SticCard {
                    Text(text = userPhrases, color = SticTextPrimary, fontSize = 14.sp)
                }
                
                SectionTitle(text = "PROCEDIMIENTO / RESPUESTA", icon = Icons.Default.Rule)
                SticCard {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = procedure,
                            color = SticBlue,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { /* Copiar */ }) {
                            Icon(Icons.Default.ContentCopy, null, tint = SticBlue, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                
                SectionTitle(text = "PALABRAS CLAVE", icon = Icons.Default.LocalOffer)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    keywords.forEach { keyword ->
                        SticChip(text = keyword)
                    }
                }
                
                SectionTitle(text = "NOTAS / COMENTARIOS", icon = Icons.Default.Notes)
                SticCard {
                    Text(text = notes, color = SticTextSecondary, fontSize = 13.sp)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IncidentDetailScreenPreview() {
    IncidentDetailScreen(incidenciaId = 1L)
}
