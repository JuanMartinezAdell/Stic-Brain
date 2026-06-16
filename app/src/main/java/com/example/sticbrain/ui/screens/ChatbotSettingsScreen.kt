package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.model.*
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla de configuración avanzada del chatbot.
 * 
 * Permite ajustar el comportamiento de la IA, el estilo de respuesta 
 * y el volumen de información procesada.
 */
@Composable
fun ChatbotSettingsScreen(
    uiState: ChatbotConfigUiState,
    onModeChange: (ChatbotMode) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onMaxContextIncidentsChange: (Int) -> Unit,
    onResponseStyleChange: (ChatbotResponseStyle) -> Unit,
    onDetailLevelChange: (ChatbotDetailLevel) -> Unit,
    onAllowExternalSearchChange: (Boolean) -> Unit,
    onSaveConfig: () -> Unit,
    onDeleteApiKey: () -> Unit,
    onConfirmGoogleAccount: () -> Unit,
    onRemoveGoogleAccount: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    var showApiKey by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SticBackground,
        topBar = {
            SticTopHeader(
                title = "IA y Configuración",
                subtitle = "Optimización del asistente",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            SticBottomBar(
                selectedItem = -1,
                onHomeClick = onNavigateToHome,
                onSearchClick = {},
                onNewIncidentClick = {},
                onSupportClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SECCIÓN: MODO DE FUNCIONAMIENTO
            SticCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Modo de funcionamiento", style = MaterialTheme.typography.titleMedium, color = SticBlue, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = uiState.mode == ChatbotMode.LOCAL, onClick = { onModeChange(ChatbotMode.LOCAL) })
                        Text("Modo Local (Búsqueda en Room)", fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = uiState.mode == ChatbotMode.GEMINI, onClick = { onModeChange(ChatbotMode.GEMINI) })
                        Text("Modo Gemini (Diagnóstico con IA)", fontSize = 14.sp)
                    }
                }
            }

            // SECCIÓN: IDENTIDAD
            SticCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Identidad de Google", style = MaterialTheme.typography.titleMedium, color = SticBlue, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (uiState.googleAccountConfirmed) {
                        Text("Cuenta: ${uiState.googleEmail}", fontSize = 14.sp, color = SticBlue)
                        TextButton(onClick = onRemoveGoogleAccount, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) {
                            Text("Quitar vinculación")
                        }
                    } else {
                        Button(onClick = onConfirmGoogleAccount, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = SticSky)) {
                            Text("Confirmar con Google", color = SticBlue)
                        }
                    }
                }
            }

            // SECCIÓN: AJUSTES DE GENERACIÓN (Solo Gemini o avanzado)
            SticCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Ajustes del Asistente", style = MaterialTheme.typography.titleMedium, color = SticBlue, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.mode == ChatbotMode.GEMINI) {
                        OutlinedTextField(
                            value = uiState.geminiApiKeyInput,
                            onValueChange = onApiKeyChange,
                            label = { Text("API Key") },
                            placeholder = { Text(if (uiState.hasGeminiApiKey) "Clave configurada" else "Introduce clave") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showApiKey = !showApiKey }) {
                                    Icon(imageVector = if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = uiState.geminiModel,
                            onValueChange = onModelChange,
                            label = { Text("Modelo (ej: gemini-1.5-flash)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Opción: Número de fichas de contexto
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = SticBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Fichas de contexto: ${uiState.maxContextIncidents}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    Slider(
                        value = uiState.maxContextIncidents.toFloat(),
                        onValueChange = { onMaxContextIncidentsChange(it.toInt()) },
                        valueRange = 3f..8f,
                        steps = 2, // Para que elija 3, 5 u 8
                        colors = SliderDefaults.colors(thumbColor = SticBlue, activeTrackColor = SticBlue)
                    )
                    Text(text = "3: Rápido | 5: Equilibrado | 8: Análisis profundo", fontSize = 11.sp, color = SticTextSecondary)
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Opción: Estilo de respuesta
                    Text(text = "Estilo de respuesta", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    ChatbotResponseStyle.entries.forEach { style ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = uiState.responseStyle == style, onClick = { onResponseStyleChange(style) })
                            Text(text = style.toReadableText(), fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Opción: Nivel de detalle
                    Text(text = "Nivel de detalle", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    ChatbotDetailLevel.entries.forEach { level ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = uiState.detailLevel == level, onClick = { onDetailLevelChange(level) })
                            Text(text = level.toReadableText(), fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Opción: Permitir búsqueda externa
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = uiState.allowExternalSearchWhenNoLocalInfo,
                            onCheckedChange = onAllowExternalSearchChange
                        )
                        Column {
                            Text(text = "Permitir búsqueda externa", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(text = "Preguntar si buscar fuera si no hay datos locales.", fontSize = 11.sp, color = SticTextSecondary)
                        }
                    }
                }
            }

            if (uiState.message != null) Text(uiState.message, color = SticBlue, fontSize = 13.sp)
            if (uiState.error != null) Text(uiState.error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)

            Button(onClick = onSaveConfig, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = SticBlue), shape = RoundedCornerShape(12.dp), enabled = !uiState.isSaving) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Cambios")
            }

            if (uiState.hasGeminiApiKey) {
                TextButton(onClick = onDeleteApiKey, modifier = Modifier.align(Alignment.CenterHorizontally), colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) {
                    Text("Borrar API Key")
                }
            }
            
            Button(onClick = onNavigateToChatbot, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = SticSky), shape = RoundedCornerShape(12.dp)) {
                Text("Probar Chatbot", color = SticBlue)
            }
        }
    }
}
