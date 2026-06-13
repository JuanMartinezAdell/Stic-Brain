package com.example.sticbrain.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.local.entity.ProveedorEntity
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla que muestra los detalles completos de un proveedor de soporte.
 * 
 * Centraliza la información de contacto, horarios y condiciones de servicio
 * para facilitar el escalado de incidencias.
 */
@Composable
fun ProviderDetailScreen(
    proveedorId: Long,
    proveedor: ProveedorEntity?,
    onNavigateBack: () -> Unit = {},
    onNavigateToEditProvider: (Long) -> Unit = {},
    onNavigateToNewProvider: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
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
            // Cabecera con botón de retroceso y edición
            SticTopHeader(
                title = "Ficha de soporte",
                subtitle = if (proveedor != null) "#P$proveedorId · ${proveedor.nombre}" else "#P$proveedorId",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = { onNavigateToEditProvider(proveedorId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = SticWhite)
                    }
                }
            )
            
            if (proveedor == null) {
                // Estado de espera mientras se cargan los datos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SticBlue)
                }
            } else {
                // Información detallada organizada por bloques
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Datos principales
                    SticCard {
                        Text(text = proveedor.nombre, color = SticTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = proveedor.servicioAsociado, color = SticBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    
                    // Bloque de contacto
                    SectionTitle(text = "CONTACTO", icon = Icons.Default.ContactPhone)
                    SticCard {
                        ContactRow(icon = Icons.Default.Phone, text = proveedor.telefono ?: "-")
                        ContactRow(icon = Icons.Default.Email, text = proveedor.email ?: "-")
                        ContactRow(icon = Icons.Default.Language, text = proveedor.web ?: "-")
                    }
                    
                    // Horario de atención
                    SectionTitle(text = "HORARIO", icon = Icons.Default.Schedule)
                    SticCard {
                        Text(text = proveedor.horario ?: "No especificado", color = SticTextPrimary, fontSize = 14.sp)
                    }
                    
                    // Acuerdo de nivel de servicio (SLA)
                    SectionTitle(text = "SLA", icon = Icons.Default.Timer)
                    SticCard {
                        Text(text = proveedor.sla ?: "No especificado", color = SticGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // Áreas o categorías en las que este proveedor interviene
                    if (!proveedor.categoriasRelacionadas.isNullOrBlank()) {
                        SectionTitle(text = "CATEGORÍAS RELACIONADAS", icon = Icons.Default.LocalOffer)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            proveedor.categoriasRelacionadas.split(",").forEach { cat ->
                                if (cat.isNotBlank()) SticChip(text = cat.trim())
                            }
                        }
                    }
                    
                    // Comentarios de gestión
                    SectionTitle(text = "NOTAS / COMENTARIOS", icon = Icons.Default.Comment)
                    SticCard {
                        Text(text = proveedor.notasComentarios ?: "-", color = SticTextSecondary, fontSize = 13.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón para añadir otro proveedor si este no es el adecuado
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
}

/** Fila auxiliar para mostrar un dato de contacto con icono. */
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
    ProviderDetailScreen(proveedorId = 1L, proveedor = null)
}
