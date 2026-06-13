package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.data.local.entity.IncidenciaEntity
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Formulario para registrar una nueva entrada en la base de conocimiento.
 * 
 * Permite introducir todos los campos requeridos por el estándar del proyecto
 * con validaciones para asegurar la calidad de la información.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewIncidentScreen(
    categorias: List<CategoriaEntity>,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onSaveIncident: (IncidenciaEntity) -> Unit = {}
) {
    // Estados internos del formulario
    var category by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var userPhrases by remember { mutableStateOf("") }
    var procedure by remember { mutableStateOf("") }
    var keywords by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Normal") }
    var notes by remember { mutableStateOf("") }

    // Estados para controlar errores de validación
    var categoryError by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var procedureError by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SticBackground,
        bottomBar = {
            SticBottomBar(
                selectedItem = 2, // Nueva entrada seleccionada
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onSupportClick = onNavigateToSupport
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Cabecera con botón de retroceso
            SticTopHeader(
                title = "Nueva entrada", 
                subtitle = "Base de conocimiento TIC", 
                showBackButton = true, 
                onBackClick = onNavigateBack
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Selector de Categoría mediante chips dinámicos
                Text(text = "CATEGORÍA *", color = if (categoryError) SticRed else SticTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                if (categorias.isEmpty()) {
                    Text(text = "No hay categorías disponibles", color = SticTextSecondary, fontSize = 14.sp)
                } else {
                    FlowRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categorias.forEach { cat ->
                            SticChip(
                                text = cat.nombre,
                                isSelected = category == cat.nombre,
                                onClick = { 
                                    category = cat.nombre
                                    categoryError = false
                                }
                            )
                        }
                    }
                }
                if (categoryError) {
                    Text(text = "Debe seleccionar una categoría técnica", color = SticRed, fontSize = 11.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campos de texto obligatorios y opcionales
                SticTextField(
                    value = title, 
                    onValueChange = { 
                        title = it
                        titleError = it.isBlank()
                    }, 
                    label = "TÍTULO / NOMBRE *", 
                    placeholder = "Ej: Acceso a Mosaiq",
                    isError = titleError,
                    errorMessage = "El título es obligatorio para identificar la ficha"
                )

                SticTextField(
                    value = description, 
                    onValueChange = { 
                        description = it
                        descriptionError = it.isBlank()
                    }, 
                    label = "DESCRIPCIÓN *", 
                    placeholder = "Breve explicación del problema...",
                    isError = descriptionError,
                    errorMessage = "La descripción es obligatoria"
                )

                SticTextField(value = userPhrases, onValueChange = { userPhrases = it }, label = "FRASES DE USUARIO", placeholder = "Lo que suele decir el usuario...")

                SticTextField(
                    value = procedure, 
                    onValueChange = { 
                        procedure = it
                        procedureError = it.isBlank()
                    }, 
                    label = "PROCEDIMIENTO / RESPUESTA *", 
                    placeholder = "Pasos técnicos para resolver la situación...", 
                    singleLine = false, 
                    modifier = Modifier.height(120.dp),
                    isError = procedureError,
                    errorMessage = "Debe incluir al menos un paso de resolución"
                )

                SticTextField(value = keywords, onValueChange = { keywords = it }, label = "PALABRAS CLAVE", placeholder = "Separadas por comas (ej: vpn, login, error)...")
                
                // Selector de Prioridad
                Text(text = "NIVEL DE PRIORIDAD *", color = SticTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Baja", "Normal", "Alta", "Crítica").forEach { prio ->
                        SticChip(
                            text = prio,
                            isSelected = priority == prio,
                            onClick = { priority = prio }
                        )
                    }
                }
                
                SticTextField(value = notes, onValueChange = { notes = it }, label = "NOTAS / COMENTARIOS", placeholder = "Cualquier observación adicional...", singleLine = false, modifier = Modifier.height(100.dp))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón principal de guardado
                Button(
                    onClick = {
                        // Validación final antes de enviar al ViewModel
                        categoryError = category.isBlank()
                        titleError = title.isBlank()
                        descriptionError = description.isBlank()
                        procedureError = procedure.isBlank()

                        if (!categoryError && !titleError && !descriptionError && !procedureError) {
                            val nuevaIncidencia = IncidenciaEntity(
                                categoria = category,
                                tituloNombre = title,
                                descripcion = description,
                                frasesUsuario = userPhrases,
                                procedimientoRespuesta = procedure,
                                palabrasClave = keywords,
                                nivelPrioridad = priority,
                                notasComentarios = notes,
                                fechaCreacion = System.currentTimeMillis()
                            )
                            onSaveIncident(nuevaIncidencia)
                        }
                    },
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
    NewIncidentScreen(categorias = emptyList())
}
