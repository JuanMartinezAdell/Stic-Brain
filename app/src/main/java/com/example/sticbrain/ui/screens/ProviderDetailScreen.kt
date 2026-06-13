package com.example.sticbrain.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.ui.components.BottomNavigationBar
import com.example.sticbrain.ui.theme.SticBrainTheme

@OptIn(ExperimentalLayoutApi::class)
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
    // Datos temporales para maquetación
    val providerName = "Siemens Healthineers"
    val serviceArea = "HIS · Soaris"
    val description = "Proveedor externo para incidencias relacionadas con Soaris HIS y servicios clínicos asociados."
    val categories = listOf("HIS", "Soaris", "Producción", "Crítica")
    
    val contactInfo = listOf(
        "Teléfono: 900 102 345",
        "Email: soporte.his@siemens-healthineers.com",
        "Web: soporte.siemens-healthineers.com",
        "Horario: L-V 08:00-20:00"
    )
    
    val escalationSla = listOf(
        "Disponibilidad: L-V",
        "SLA crítica: 4h",
        "SLA alta: 8h",
        "Canal preferente: Teléfono + ticket"
    )
    
    val associatedServices = listOf("Soaris HIS", "Gestión de sesiones", "Worklist clínico", "Integraciones hospitalarias")
    
    val observations = "Escalar incidencias críticas cuando afecten a varios usuarios o servicios asistenciales. Indicar siempre entorno, mensaje de error, hora de inicio e impacto funcional."

    Scaffold(
        containerColor = Color(0xFF0D1117),
        topBar = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color(0xFF58A6FF)
                            )
                        }
                        Column {
                            Text(
                                text = "DETALLE DE PROVEEDOR",
                                color = Color(0xFF8B949E),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "#P$proveedorId · ",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Soporte externo",
                                    color = Color(0xFF8B949E),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onNavigateToEditProvider(proveedorId) },
                            modifier = Modifier
                                .background(Color(0xFF5865F2).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color(0xFF5865F2),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onNavigateToNewProvider,
                            modifier = Modifier
                                .background(Color(0xFF3FB950).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Nuevo",
                                tint = Color(0xFF3FB950),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 3, // Soporte is index 3
                onHomeClick = onNavigateToHome,
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Nombre y Área
            Text(
                text = providerName,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = serviceArea,
                color = Color(0xFF58A6FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                color = Color(0xFFC9D1D9),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Chips de categorías
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    Surface(
                        color = Color(0xFF21262D),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF30363D))
                    ) {
                        Text(
                            text = category,
                            color = Color(0xFF8B949E),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Bloque de Contacto
            Surface(
                color = Color(0xFF161B22),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF30363D))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ContactPhone,
                            contentDescription = null,
                            tint = Color(0xFF58A6FF),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONTACTO",
                            color = Color(0xFF58A6FF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    contactInfo.forEach { info ->
                        val parts = info.split(": ", limit = 2)
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "${parts[0]}: ",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (parts.size > 1) {
                                Text(
                                    text = parts[1],
                                    color = Color(0xFFC9D1D9),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Bloque de Escalado y SLA
            Surface(
                color = Color(0xFF161B22),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF30363D))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = Color(0xFF3FB950),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ESCALADO Y SLA",
                            color = Color(0xFF3FB950),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    escalationSla.forEach { info ->
                        val parts = info.split(": ", limit = 2)
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "${parts[0]}: ",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (parts.size > 1) {
                                Text(
                                    text = parts[1],
                                    color = Color(0xFFC9D1D9),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Bloque de Servicios Asociados
            Surface(
                color = Color(0xFF161B22),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF30363D))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SettingsApplications,
                            contentDescription = null,
                            tint = Color(0xFFA371F7),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SERVICIOS ASOCIADOS",
                            color = Color(0xFFA371F7),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    associatedServices.forEach { service ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(Color(0xFFA371F7), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = service,
                                color = Color(0xFFC9D1D9),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Bloque de Observaciones
            Surface(
                color = Color(0xFF161B22),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF30363D))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFD29922),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OBSERVACIONES",
                            color = Color(0xFFD29922),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = observations,
                        color = Color(0xFFC9D1D9),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botones de acción
            Button(
                onClick = { onNavigateToEditProvider(proveedorId) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar proveedor", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onNavigateToNewProvider,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nuevo proveedor", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProviderDetailScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        ProviderDetailScreen(proveedorId = 1L)
    }
}
