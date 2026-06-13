package com.example.sticbrain.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.local.entity.ProveedorEntity
import com.example.sticbrain.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProviderFormScreen(
    proveedorId: Long? = null,
    proveedor: ProveedorEntity? = null,
    isEditMode: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onSaveProvider: (ProveedorEntity) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var serviceArea by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var web by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var sla by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    
    val categories = listOf("Red", "HIS", "PACS", "Impresoras", "Correo", "VPN", "Servidores", "AD/GPO", "SAP", "General")
    val selectedCategories = remember { mutableStateListOf<String>() }

    // Rellenar datos si es modo edición y llega el proveedor
    LaunchedEffect(proveedor) {
        if (isEditMode && proveedor != null) {
            name = proveedor.nombre
            serviceArea = proveedor.servicioAsociado
            phone = proveedor.telefono ?: ""
            email = proveedor.email ?: ""
            web = proveedor.web ?: ""
            schedule = proveedor.horario ?: ""
            sla = proveedor.sla ?: ""
            notes = proveedor.notasComentarios ?: ""
            
            selectedCategories.clear()
            proveedor.categoriasRelacionadas?.split(",")?.forEach {
                if (it.isNotBlank()) selectedCategories.add(it.trim())
            }
        }
    }

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
                title = if (isEditMode) "Editar proveedor" else "Nuevo proveedor",
                subtitle = if (isEditMode) "Proveedor #P$proveedorId" else "Directorio de soporte",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                SticTextField(value = name, onValueChange = { name = it }, label = "NOMBRE DEL SOPORTE/PROVEEDOR *", placeholder = "Ej: Siemens Healthineers")
                SticTextField(value = serviceArea, onValueChange = { serviceArea = it }, label = "SERVICIO ASOCIADO *", placeholder = "Ej: HIS · Soaris")
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        SticTextField(value = phone, onValueChange = { phone = it }, label = "TELÉFONO", placeholder = "900 102 345")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        SticTextField(value = email, onValueChange = { email = it }, label = "EMAIL", placeholder = "soporte@proveedor.com")
                    }
                }
                
                SticTextField(value = web, onValueChange = { web = it }, label = "WEB", placeholder = "www.proveedor.com")
                SticTextField(value = schedule, onValueChange = { schedule = it }, label = "HORARIO", placeholder = "Ej: L-V 08:00-20:00")
                SticTextField(value = sla, onValueChange = { sla = it }, label = "SLA", placeholder = "Ej: 4h crítica")
                
                Text(text = "CATEGORÍAS RELACIONADAS", color = SticTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                FlowRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategories.contains(cat)
                        SticChip(
                            text = cat,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelected) selectedCategories.remove(cat)
                                else selectedCategories.add(cat)
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                SticTextField(value = notes, onValueChange = { notes = it }, label = "NOTAS / COMENTARIOS", placeholder = "Condiciones de escalado...", singleLine = false, modifier = Modifier.height(100.dp))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, SticBlue)
                    ) {
                        Text("Cancelar", color = SticBlue, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            if (name.isNotBlank() && serviceArea.isNotBlank()) {
                                val nuevoProveedor = ProveedorEntity(
                                    id = proveedorId ?: 0L,
                                    nombre = name,
                                    servicioAsociado = serviceArea,
                                    telefono = phone,
                                    email = email,
                                    web = web,
                                    horario = schedule,
                                    sla = sla,
                                    categoriasRelacionadas = selectedCategories.joinToString(","),
                                    notasComentarios = notes
                                )
                                onSaveProvider(nuevoProveedor)
                            }
                        },
                        modifier = Modifier.weight(1f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SticBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isEditMode) "Actualizar" else "Guardar", color = SticWhite, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProviderFormScreenPreview() {
    ProviderFormScreen()
}
