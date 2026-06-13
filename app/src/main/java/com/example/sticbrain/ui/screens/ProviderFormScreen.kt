package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProviderFormScreen(
    proveedorId: Long? = null,
    isEditMode: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onSaveProvider: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    // Estados temporales del formulario
    var name by remember { mutableStateOf("") }
    var serviceArea by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var web by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var sla by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }
    
    val categories = listOf("Red", "HIS", "PACS", "Impresoras", "Correo", "VPN", "Servidores", "AD/GPO", "SAP", "General")
    val selectedCategories = remember { mutableStateListOf<String>() }
    
    val availabilities = listOf("L-V", "24/7", "Horario ampliado", "Bajo demanda")
    var selectedAvailability by remember { mutableStateOf("L-V") }

    // Validación temporal
    var nameError by remember { mutableStateOf(false) }
    var serviceError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    // Simular carga de datos si es modo edición
    LaunchedEffect(isEditMode) {
        if (isEditMode && proveedorId != null) {
            // Datos temporales de ejemplo
            name = "Siemens Healthineers"
            serviceArea = "HIS · Soaris"
            phone = "900 102 345"
            email = "soporte.his@siemens-healthineers.com"
            web = "soporte.siemens-healthineers.com"
            schedule = "L-V 08:00-20:00"
            sla = "4h crítica / 8h alta"
            observations = "Escalar incidencias críticas cuando afecten a varios usuarios."
            selectedCategories.add("HIS")
            selectedAvailability = "L-V"
        }
    }

    Scaffold(
        containerColor = Color(0xFF0D1117),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 3, // Soporte
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
            ProviderFormHeader(isEditMode, proveedorId)
            
            Spacer(modifier = Modifier.height(24.dp))

            // CAMPOS DEL FORMULARIO
            ProviderTextField(
                label = "NOMBRE DEL PROVEEDOR *",
                value = name,
                onValueChange = { 
                    name = it
                    nameError = it.isBlank()
                },
                placeholder = "Ej: Siemens Healthineers",
                isError = nameError,
                errorMessage = "El nombre es obligatorio"
            )

            ProviderTextField(
                label = "SERVICIO O ÁREA TÉCNICA *",
                value = serviceArea,
                onValueChange = { 
                    serviceArea = it
                    serviceError = it.isBlank()
                },
                placeholder = "Ej: HIS · Soaris",
                isError = serviceError,
                errorMessage = "El área técnica es obligatoria"
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    ProviderTextField(
                        label = "TELÉFONO",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "900 102 345"
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ProviderTextField(
                        label = "HORARIO",
                        value = schedule,
                        onValueChange = { schedule = it },
                        placeholder = "Ej: L-V 08:00-20:00"
                    )
                }
            }

            ProviderTextField(
                label = "EMAIL",
                value = email,
                onValueChange = { 
                    email = it
                    emailError = it.isNotEmpty() && !it.contains("@")
                },
                placeholder = "Ej: soporte.his@proveedor.com",
                isError = emailError,
                errorMessage = "Email no válido (falta @)"
            )

            ProviderTextField(
                label = "WEB",
                value = web,
                onValueChange = { web = it },
                placeholder = "Ej: soporte.proveedor.com"
            )

            ProviderTextField(
                label = "SLA",
                value = sla,
                onValueChange = { sla = it },
                placeholder = "Ej: 4h crítica / 8h alta"
            )

            ProviderTextField(
                label = "OBSERVACIONES",
                value = observations,
                onValueChange = { observations = it },
                placeholder = "Indicar condiciones de escalado...",
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CATEGORÍAS
            Text(
                text = "CATEGORÍAS ASOCIADAS",
                color = Color(0xFF8B949E),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategories.contains(category)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedCategories.remove(category)
                            else selectedCategories.add(category)
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF161B22),
                            labelColor = Color(0xFF8B949E),
                            selectedContainerColor = Color(0xFF5865F2).copy(alpha = 0.2f),
                            selectedLabelColor = Color(0xFF5865F2)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color(0xFF30363D),
                            selectedBorderColor = Color(0xFF5865F2),
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DISPONIBILIDAD
            Text(
                text = "DISPONIBILIDAD",
                color = Color(0xFF8B949E),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availabilities.forEach { availability ->
                    val isSelected = selectedAvailability == availability
                    Surface(
                        onClick = { selectedAvailability = availability },
                        color = if (isSelected) Color(0xFF3FB950).copy(alpha = 0.1f) else Color(0xFF161B22),
                        shape = RoundedCornerShape(8.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3FB950)) else null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = availability,
                            color = if (isSelected) Color(0xFF3FB950) else Color(0xFF8B949E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ACCIONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        // Validación rápida
                        nameError = name.isBlank()
                        serviceError = serviceArea.isBlank()
                        emailError = email.isNotEmpty() && !email.contains("@")

                        if (!nameError && !serviceError && !emailError) {
                            onSaveProvider()
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5865F2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isEditMode) "Actualizar" else "Guardar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProviderFormHeader(isEditMode: Boolean, proveedorId: Long?) {
    Column {
        Text(
            text = if (isEditMode) "Editar proveedor" else "Nuevo proveedor",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        if (isEditMode && proveedorId != null) {
            Text(
                text = "Proveedor #P$proveedorId",
                color = Color(0xFF8B949E),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ProviderTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = "",
    singleLine: Boolean = true
) {
    Column(modifier = modifier.padding(bottom = 20.dp)) {
        Text(
            text = label,
            color = Color(0xFF8B949E),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color(0xFF484F58), fontSize = 14.sp) },
            isError = isError,
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF161B22),
                unfocusedContainerColor = Color(0xFF161B22),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFF5865F2),
                focusedIndicatorColor = Color(0xFF5865F2),
                unfocusedIndicatorColor = Color.Transparent,
                errorContainerColor = Color(0xFF161B22),
                errorIndicatorColor = Color(0xFFF85149)
            )
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = Color(0xFFF85149),
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProviderFormScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        ProviderFormScreen()
    }
}
