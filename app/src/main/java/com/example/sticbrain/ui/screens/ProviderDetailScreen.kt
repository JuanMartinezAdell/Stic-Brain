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
import com.example.sticbrain.ui.theme.*

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
            SticTopHeader(
                title = "Ficha de soporte",
                subtitle = "#P$proveedorId · Soporte externo",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = { onNavigateToEditProvider(proveedorId) }) {
                        Icon(Icons.Default.Edit, null, tint = SticWhite)
                    }
                }
            )
            
            if (proveedor == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SticBlue)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    SticCard {
                        Text(text = proveedor.nombre, color = SticTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = proveedor.servicioAsociado, color = SticBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    
                    SectionTitle(text = "CONTACTO", icon = Icons.Default.ContactPhone)
                    SticCard {
                        ContactRow(icon = Icons.Default.Phone, text = proveedor.telefono ?: "-")
                        ContactRow(icon = Icons.Default.Email, text = proveedor.email ?: "-")
                        ContactRow(icon = Icons.Default.Language, text = proveedor.web ?: "-")
                    }
                    
                    SectionTitle(text = "HORARIO", icon = Icons.Default.Schedule)
                    SticCard {
                        Text(text = proveedor.horario ?: "-", color = SticTextPrimary, fontSize = 14.sp)
                    }
                    
                    SectionTitle(text = "SLA", icon = Icons.Default.Timer)
                    SticCard {
                        Text(text = proveedor.sla ?: "-", color = SticGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    if (!proveedor.categoriasRelacionadas.isNullOrBlank()) {
                        SectionTitle(text = "CATEGORÍAS RELACIONADAS", icon = Icons.Default.LocalOffer)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            proveedor.categoriasRelacionadas.split(",").forEach { cat ->
                                SticChip(text = cat.trim())
                            }
                        }
                    }
                    
                    SectionTitle(text = "NOTAS / COMENTARIOS", icon = Icons.Default.Notes)
                    SticCard {
                        Text(text = proveedor.notasComentarios ?: "-", color = SticTextSecondary, fontSize = 13.sp)
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
    ProviderDetailScreen(proveedorId = 1L, proveedor = null)
}
