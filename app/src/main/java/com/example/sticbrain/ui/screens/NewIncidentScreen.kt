package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
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
import com.example.sticbrain.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewIncidentScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSaveIncident: () -> Unit = {}
) {
    var category by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var userPhrases by remember { mutableStateOf("") }
    var procedure by remember { mutableStateOf("") }
    var keywords by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Normal") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf(
        "Acceso y autenticación", "Hardware", "Red y conectividad", "Software",
        "Correo electrónico", "Seguridad", "Sistemas operativos", "Impresión y escaneo",
        "Servicios y aplicaciones internas", "Telefonía", "Soporte remoto"
    )

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 2,
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            SticTopHeader(title = "Nueva entrada", subtitle = "Base de conocimiento TIC", showBackButton = true, onBackClick = onNavigateBack)
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(text = "CATEGORÍA *", color = SticTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                FlowRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        SticChip(
                            text = cat,
                            isSelected = category == cat,
                            onClick = { category = cat }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                SticTextField(value = title, onValueChange = { title = it }, label = "TÍTULO / NOMBRE *", placeholder = "Ej: Acceso a Mosaiq")
                SticTextField(value = description, onValueChange = { description = it }, label = "DESCRIPCIÓN *", placeholder = "Breve explicación...")
                SticTextField(value = userPhrases, onValueChange = { userPhrases = it }, label = "FRASES DE USUARIO", placeholder = "Lo que dice el usuario...")
                SticTextField(value = procedure, onValueChange = { procedure = it }, label = "PROCEDIMIENTO / RESPUESTA *", placeholder = "Pasos a seguir...", singleLine = false, modifier = Modifier.height(100.dp))
                SticTextField(value = keywords, onValueChange = { keywords = it }, label = "PALABRAS CLAVE", placeholder = "Separadas por comas...")
                
                Text(text = "NIVEL DE PRIORIDAD", color = SticTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Baja", "Normal", "Alta", "Crítica").forEach { prio ->
                        SticChip(
                            text = prio,
                            isSelected = priority == prio,
                            onClick = { priority = prio }
                        )
                    }
                }
                
                SticTextField(value = notes, onValueChange = { notes = it }, label = "NOTAS / COMENTARIOS", placeholder = "Información adicional...", singleLine = false, modifier = Modifier.height(80.dp))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onSaveIncident,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SticBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Guardar entrada", color = SticWhite, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewIncidentScreenPreview() {
    NewIncidentScreen()
}
