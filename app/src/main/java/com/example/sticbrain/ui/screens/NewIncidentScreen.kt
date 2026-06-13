package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NewIncidentScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSaveIncident: () -> Unit = {}
) {
    // Estados temporales del formulario
    var selectedCategory by remember { mutableStateOf("HIS") }
    var selectedSeverity by remember { mutableStateOf("Crítica") }
    var applicationName by remember { mutableStateOf("") }
    var selectedEnvironment by remember { mutableStateOf("Producción") }
    var keywords by remember { mutableStateOf("") }
    var selectedProvider by remember { mutableStateOf("Sin escalado externo") }

    val categories = listOf("Red", "HIS", "PACS", "Impresoras", "Correo", "VPN", "Servidores", "Workstations", "AD/GPO", "SAP")
    val severities = listOf("Crítica", "Alta", "Media", "Baja")
    val environments = listOf("Producción", "Preproducción", "Desarrollo", "Terminal Clínico", "Puesto Administrativo")
    val providers = listOf("Sin escalado externo", "Siemens Healthineers", "Sectra", "FortiGate", "Soporte CAU", "Proveedor impresoras")

    Scaffold(
        containerColor = Color(0xFF0D1117),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 2, // Nueva is index 2
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Nueva incidencia",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Paso 2 de 2",
                        color = Color(0xFF8B949E),
                        fontSize = 14.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(24.dp, 4.dp).background(Color(0xFF5865F2), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.size(24.dp, 4.dp).background(Color(0xFF5865F2), RoundedCornerShape(2.dp)))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // CATEGORÍA
            FormSectionTitle("CATEGORÍA *")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    SelectableChip(
                        text = category,
                        isSelected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        color = getCategoryColor(category)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // GRAVEDAD
            FormSectionTitle("GRAVEDAD *")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                severities.forEach { severity ->
                    SelectableChip(
                        text = severity,
                        isSelected = selectedSeverity == severity,
                        onClick = { selectedSeverity = severity },
                        color = getSeverityColor(severity),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // APLICACIÓN
            FormSectionTitle("APLICACIÓN *")
            OutlinedTextField(
                value = applicationName,
                onValueChange = { applicationName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: Soaris HIS, Sectra PACS...", color = Color(0xFF484F58)) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF161B22),
                    unfocusedContainerColor = Color(0xFF161B22),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ENTORNO
            FormSectionTitle("ENTORNO")
            CustomDropdown(
                selectedOption = selectedEnvironment,
                options = environments,
                onOptionSelected = { selectedEnvironment = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // PALABRAS CLAVE
            FormSectionTitle("PALABRAS CLAVE")
            OutlinedTextField(
                value = keywords,
                onValueChange = { keywords = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Separadas por comas: vpn, timeout, login", color = Color(0xFF484F58)) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF161B22),
                    unfocusedContainerColor = Color(0xFF161B22),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // PROVEEDOR DE SOPORTE
            FormSectionTitle("PROVEEDOR DE SOPORTE")
            CustomDropdown(
                selectedOption = selectedProvider,
                options = providers,
                onOptionSelected = { selectedProvider = it }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // BOTONES INFERIORES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Anterior", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onSaveIncident,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5865F2)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Guardar incidencia", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FormSectionTitle(text: String) {
    Text(
        text = text,
        color = Color(0xFF8B949E),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.2f) else Color(0xFF161B22),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, color) else null
    ) {
        Text(
            text = text,
            color = if (isSelected) color else Color(0xFF8B949E),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun CustomDropdown(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            color = Color(0xFF161B22),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = selectedOption, color = Color.White, fontSize = 14.sp)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF484F58))
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF161B22))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun getCategoryColor(category: String): Color {
    return when (category) {
        "Red" -> Color(0xFF58A6FF)
        "HIS" -> Color(0xFFA371F7)
        "PACS" -> Color(0xFF388BFD)
        "Impresoras" -> Color(0xFFDB6D28)
        "Correo" -> Color(0xFFD29922)
        "VPN" -> Color(0xFF3FB950)
        "Servidores" -> Color(0xFFF85149)
        "Workstations" -> Color(0xFF8B949E)
        "AD/GPO" -> Color(0xFFF06292)
        "SAP" -> Color(0xFFFFB74D)
        else -> Color.White
    }
}

fun getSeverityColor(severity: String): Color {
    return when (severity) {
        "Crítica" -> Color(0xFFF85149)
        "Alta" -> Color(0xFFDB6D28)
        "Media" -> Color(0xFFD29922)
        "Baja" -> Color(0xFF3FB950)
        else -> Color.White
    }
}

@Preview(showBackground = true)
@Composable
fun NewIncidentScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        NewIncidentScreen()
    }
}
