package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.model.ChatbotIncidentResult
import com.example.sticbrain.data.model.ChatMessage
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla del Chatbot de conocimiento con persistencia e historial.
 */
@Composable
fun ChatbotScreen(
    messages: List<ChatMessage>,
    currentQuestion: String,
    isLoading: Boolean,
    chatbotModeLabel: String = "Modo local", // Añadido para mostrar el modo
    onQuestionChange: (String) -> Unit,
    onSendQuestion: () -> Unit,
    onClearConversation: () -> Unit,
    onOpenIncidentDetail: (Long) -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Hacer scroll automático al último mensaje cuando cambia la lista
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Diálogo de confirmación para borrar el historial
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Borrar historial") },
            text = { Text("¿Quieres borrar todo el historial del chatbot? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearConversation()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Borrar historial")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        containerColor = SticBackground,
        topBar = {
            SticTopHeader(
                title = "Asistente TIC",
                subtitle = chatbotModeLabel,
                showBackButton = true,
                onBackClick = onNavigateToHome,
                actions = {
                    if (messages.isNotEmpty()) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Limpiar chat", tint = SticWhite)
                        }
                    }
                }
            )
        },
        bottomBar = {
            SticBottomBar(
                selectedItem = -1,
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
        ) {
            // Área de conversación
            Box(modifier = Modifier.weight(1f)) {
                if (messages.isEmpty()) {
                    WelcomeMessage()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(messages) { message ->
                            ChatBubble(message, onOpenIncidentDetail)
                        }
                        if (isLoading) {
                            item {
                                TypingIndicator()
                            }
                        }
                    }
                }
            }

            // Barra de entrada de texto
            Surface(
                color = SticWhite,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentQuestion,
                        onValueChange = onQuestionChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("¿En qué puedo ayudarte?", fontSize = 14.sp) },
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3,
                        enabled = !isLoading,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SticBlue,
                            unfocusedBorderColor = SticBorder
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = onSendQuestion,
                        containerColor = if (currentQuestion.isBlank()) SticBorder else SticBlue,
                        contentColor = SticWhite,
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                    }
                }
            }
        }
    }
}

/** Componente de bienvenida con ejemplos de uso. */
@Composable
private fun WelcomeMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SticCard {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Chat, contentDescription = null, tint = SticBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Soy el asistente de Stic Brain",
                        color = SticBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Busco fichas técnicas relacionadas con tu pregunta y te muestro los mejores resultados.",
                    color = SticTextPrimary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Prueba a preguntarme:",
                    color = SticTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "• ¿Qué revisar si no hay red?", fontSize = 13.sp, color = SticTextSecondary)
                Text(text = "• Problemas con la impresora", fontSize = 13.sp, color = SticTextSecondary)
                Text(text = "• El usuario no puede entrar", fontSize = 13.sp, color = SticTextSecondary)
            }
        }
    }
}

/** 
 * Burbuja de chat. Si el mensaje es del bot y tiene resultados, 
 * los muestra debajo del texto.
 */
@Composable
private fun ChatBubble(
    message: ChatMessage,
    onOpenDetail: (Long) -> Unit
) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (message.isUser) SticBlue else SticSky
    val txtColor = if (message.isUser) SticWhite else SticTextPrimary
    val shape = if (message.isUser) {
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
    }

    val colAlignment = if (message.isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = colAlignment
    ) {
        Surface(
            color = bgColor,
            shape = shape,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = txtColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                fontSize = 14.sp
            )
        }

        // Si hay resultados de fichas, se muestran como una lista de tarjetas
        if (!message.isUser && message.relatedIncidents.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            message.relatedIncidents.forEach { result ->
                ChatbotIncidentResultCard(result, onOpenDetail)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/** 
 * Tarjeta individual que representa una ficha técnica encontrada.
 */
@Composable
fun ChatbotIncidentResultCard(
    result: ChatbotIncidentResult,
    onOpenIncidentDetail: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(start = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SticWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SticSky)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.tituloNombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = SticBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = SticBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Rel: ${result.puntuacion}",
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = SticBlue
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = result.categoria, fontSize = 11.sp, color = SticTextSecondary)
                Text(text = " • ", fontSize = 11.sp, color = SticTextSecondary)
                Text(text = result.nivelPrioridad, fontSize = 11.sp, color = if (result.nivelPrioridad == "Urgente") Color.Red else SticTextSecondary)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = result.procedimientoResumen,
                style = MaterialTheme.typography.bodySmall,
                color = SticTextPrimary,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(
                onClick = { onOpenIncidentDetail(result.incidenciaId) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Ver detalle completo", fontSize = 12.sp, color = SticBlue)
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

/** Indicador visual de espera. */
@Composable
private fun TypingIndicator() {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            color = SticBlue,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Analizando base de conocimiento...", color = SticTextSecondary, fontSize = 12.sp)
    }
}
