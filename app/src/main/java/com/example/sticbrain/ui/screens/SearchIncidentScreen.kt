package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

@Composable
fun SearchIncidentScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToIncidentDetail: (Long) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // Datos temporales adaptados al Excel
    val allEntries = remember {
        listOf(
            KnowledgeEntryTemp(1L, "Acceso y autenticación", "Acceso a Mosaiq", "Usuario solicita el acceso a la aplicación Mosaiq", "Acceso a Mosaiq", "Incluir el grupo SE00_GA_MosaiqRegUsuarios", listOf("mosaiq", "radiofísica"), "Normal"),
            KnowledgeEntryTemp(2L, "Acceso y autenticación", "Acceso carpetas compartidas NAS", "Usuario solicita acceso a carpeta compartida de la NAS", "Solicito acceso a carpeta compartida", "El acceso es gestionado por Sevilla NAS", listOf("NAS", "carpeta compartida"), "Normal"),
            KnowledgeEntryTemp(3L, "Acceso y autenticación", "Acceso a Omnicel", "Usuario solicita acceso a Omnicel", "Solicito acceso a Omnicel", "Las credenciales son proporcionadas por el Supervisor", listOf("omnicel", "armario", "farmacia"), "Normal"),
            KnowledgeEntryTemp(4L, "Software", "Error firma digital", "Error al intentar firmar documentos", "No puedo firmar", "Verificar versión de Autofirma", listOf("firma", "certificado"), "Alta"),
            KnowledgeEntryTemp(5L, "Impresión y escaneo", "Impresora no imprime", "Impresora de planta no responde", "No imprime", "Reiniciar cola de impresión", listOf("impresora", "zebra"), "Media")
        )
    }

    val filteredEntries = remember(searchQuery) {
        if (searchQuery.isBlank()) allEntries
        else allEntries.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.procedure.contains(searchQuery, ignoreCase = true) ||
            it.keywords.any { k -> k.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 1,
                onHomeClick = onNavigateToHome,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            SticTopHeader(title = "Buscador", subtitle = "Base de conocimiento TIC")
            
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar por título, frase de usuario, procedimiento...", color = SticTextSecondary, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = SticBlue) },
                    trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Clear, null) } },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SticWhite,
                        unfocusedContainerColor = SticWhite,
                        focusedBorderColor = SticBlue,
                        unfocusedBorderColor = SticBorder
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${filteredEntries.size} Entradas en la base de conocimiento",
                    color = SticTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredEntries) { entry ->
                        IncidentEntryCard(
                            category = entry.category,
                            title = entry.title,
                            priority = entry.priority,
                            keywords = entry.keywords,
                            procedure = entry.procedure,
                            onClick = { onNavigateToIncidentDetail(entry.id) }
                        )
                    }
                }
            }
        }
    }
}

data class KnowledgeEntryTemp(
    val id: Long,
    val category: String,
    val title: String,
    val description: String,
    val userPhrases: String,
    val procedure: String,
    val keywords: List<String>,
    val priority: String
)

@Preview(showBackground = true)
@Composable
fun SearchIncidentScreenPreview() {
    SearchIncidentScreen()
}
