package com.example.sticbrain.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.model.ChatbotConfigUiState
import com.example.sticbrain.data.model.ChatbotMode
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla de configuración del chatbot.
 * 
 * Permite alternar entre modo local y Gemini, y gestionar la API key.
 */
@Composable
fun ChatbotSettingsScreen(
    uiState: ChatbotConfigUiState,
    onModeChange: (ChatbotMode) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
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
                subtitle = "Gestiona el motor del chatbot",
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
            // Sección de modo de funcionamiento
            SticCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Modo de funcionamiento",
                        style = MaterialTheme.typography.titleMedium,
                        color = SticBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.mode == ChatbotMode.LOCAL,
                            onClick = { onModeChange(ChatbotMode.LOCAL) }
                        )
                        Text("Modo Local (Sin Internet)", fontSize = 14.sp)
                    }
                    Text(
                        text = "Busca únicamente en las fichas guardadas localmente.",
                        fontSize = 12.sp,
                        color = SticTextSecondary,
                        modifier = Modifier.padding(start = 48.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.mode == ChatbotMode.GEMINI,
                            onClick = { onModeChange(ChatbotMode.GEMINI) }
                        )
                        Text("Modo Gemini (Requiere API Key)", fontSize = 14.sp)
                    }
                    Text(
                        text = "Usa IA generativa para dar respuestas más completas basadas en tus fichas.",
                        fontSize = 12.sp,
                        color = SticTextSecondary,
                        modifier = Modifier.padding(start = 48.dp)
                    )
                }
            }

            // Sección de Cuenta Google
            SticCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Identidad de Google",
                        style = MaterialTheme.typography.titleMedium,
                        color = SticBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (uiState.googleAccountConfirmed) {
                        Text("Cuenta confirmada:", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(uiState.googleEmail ?: "", fontSize = 14.sp, color = SticBlue)
                        if (uiState.googleDisplayName != null) {
                            Text(uiState.googleDisplayName, fontSize = 12.sp, color = SticTextSecondary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = onRemoveGoogleAccount,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Quitar cuenta confirmada")
                        }
                    } else {
                        Text(
                            "Debes confirmar tu identidad con una cuenta de Google para habilitar las funciones de IA externa.",
                            fontSize = 13.sp,
                            color = SticTextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onConfirmGoogleAccount,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SticSky),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirmar con Google", color = SticBlue)
                        }
                    }
                }
            }

            // Sección de API Key (Solo si Gemini está seleccionado o hay una clave)
            if (uiState.mode == ChatbotMode.GEMINI) {
                SticCard {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Configuración de Gemini",
                            style = MaterialTheme.typography.titleMedium,
                            color = SticBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = uiState.geminiApiKey,
                            onValueChange = onApiKeyChange,
                            label = { Text("Gemini API Key") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { showApiKey = !showApiKey }) {
                                    Icon(
                                        imageVector = if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = uiState.geminiModel,
                            onValueChange = onModelChange,
                            label = { Text("Modelo (ej: gemini-pro)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Aviso: La clave se guarda localmente en el dispositivo. No compartas tu API key con nadie.",
                            fontSize = 11.sp,
                            color = SticTextSecondary,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }

            // Mensajes de éxito o error
            if (uiState.message != null) {
                Text(uiState.message, color = SticBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            if (uiState.error != null) {
                Text(uiState.error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            // Botones de acción
            Button(
                onClick = onSaveConfig,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SticBlue),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isSaving
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Configuración")
            }

            if (uiState.geminiApiKey.isNotEmpty()) {
                TextButton(
                    onClick = onDeleteApiKey,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar API Key y volver a Modo Local")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onNavigateToChatbot,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SticSky),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ir al Chatbot", color = SticBlue)
            }
        }
    }
}
