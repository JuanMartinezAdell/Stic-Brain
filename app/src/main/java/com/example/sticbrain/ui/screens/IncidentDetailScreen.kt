package com.example.sticbrain.ui.screens

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
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla de detalle de una ficha de conocimiento.
 * 
 * Muestra toda la información documentada sobre un procedimiento o incidencia,
 * incluyendo descripción, causa, resolución y datos de escalado.
 */
@Composable
fun IncidentDetailScreen(
    incidenciaId: Long,
    incidencia: IncidenciaEntity?,
    onNavigateBack: () -> Unit = {},
    onDeleteIncident: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = -1, // Pantalla de detalle (no marcada en barra)
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Cabecera con título de ficha e ID
            SticTopHeader(
                title = "Ficha de conocimiento",
                subtitle = if (incidencia != null) "#$incidenciaId · ${incidencia.categoria}" else "#$incidenciaId",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = onDeleteIncident) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = SticWhite)
                    }
                }
            )
            
            if (incidencia == null) {
                // Estado de carga mientras Room recupera los datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = SticBlue)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Cargando ficha de conocimiento...", color = SticTextSecondary)
                    }
                }
            } else {
                // Contenido principal con scroll vertical
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Título y Prioridad
                    SticCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = incidencia.categoria, color = SticBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            PriorityBadge(priority = incidencia.nivelPrioridad)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = incidencia.tituloNombre, color = SticTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    SectionTitle(text = "DESCRIPCIÓN", icon = Icons.Default.Description)
                    SticCard {
                        Text(text = incidencia.descripcion, color = SticTextPrimary, fontSize = 14.sp)
                    }
                    
                    if (incidencia.frasesUsuario.isNotBlank()) {
                        SectionTitle(text = "FRASES DE USUARIO", icon = Icons.Default.ChatBubbleOutline)
                        SticCard {
                            Text(text = incidencia.frasesUsuario, color = SticTextPrimary, fontSize = 14.sp)
                        }
                    }
                    
                    SectionTitle(text = "PROCEDIMIENTO / RESPUESTA", icon = Icons.Default.CheckCircle)
                    SticCard {
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = incidencia.procedimientoRespuesta,
                                color = SticBlue,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { /* Lógica de copiar al portapapeles */ }) {
                                Icon(Icons.Default.ContentCopy, null, tint = SticBlue, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                    
                    if (incidencia.palabrasClave.isNotBlank()) {
                        SectionTitle(text = "PALABRAS CLAVE", icon = Icons.Default.LocalOffer)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            incidencia.palabrasClave.split(",").forEach { keyword ->
                                if (keyword.isNotBlank()) SticChip(text = keyword.trim())
                            }
                        }
                    }
                    
                    if (!incidencia.notasComentarios.isNullOrBlank()) {
                        SectionTitle(text = "NOTAS / COMENTARIOS", icon = Icons.Default.Comment)
                        SticCard {
                            Text(text = incidencia.notasComentarios, color = SticTextSecondary, fontSize = 13.sp)
                        }
                    }
                    
                    // Pie de página para dar aire al diseño
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IncidentDetailScreenPreview() {
    IncidentDetailScreen(incidenciaId = 1L, incidencia = null)
}
