package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.data.model.ChatbotIncidentResult
import com.example.sticbrain.data.model.ChatMessage
import com.example.sticbrain.data.model.GeneratedIncidentDraft
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*

/**
 * Pantalla del Chatbot de conocimiento con persistencia e IA mejorada.
 */
@Composable
fun ChatbotScreen(
    messages: List<ChatMessage>,
    currentQuestion: String,
    isLoading: Boolean,
    chatbotModeLabel: String = "Modo local",
    onQuestionChange: (String) -> Unit,
    onSendQuestion: () -> Unit,
    onConfirmExternalSearch: () -> Unit = {},
    onCancelExternalSearch: () -> Unit = {},
    hasPendingAction: Boolean = false,
    onCreateDraftIncident: (GeneratedIncidentDraft) -> Unit = {},
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

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

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
                            val isLast = messages.lastOrNull() == message
                            ChatBubble(
                                message = message, 
                                onOpenDetail = onOpenIncidentDetail,
                                onConfirmExternal = onConfirmExternalSearch,
                                onCancelExternal = onCancelExternalSearch,
                                showActionButtons = isLast && hasPendingAction,
                                onCreateDraft = onCreateDraftIncident
                            )
                        }
                        if (isLoading) {
                            item {
                                TypingIndicator()
                            }
                        }
                    }
                }
            }

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
                        placeholder = { Text("Pregunta sobre un procedimiento...", fontSize = 14.sp) },
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

@Composable
private fun WelcomeMessage() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SticCard {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SticBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Asistente de Stic Brain", color = SticBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Puedo analizar varias fichas de conocimiento y proponerte la solución más probable.",
                    color = SticTextPrimary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Ejemplos:", color = SticTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "• ¿Qué revisar si no hay red?", fontSize = 13.sp, color = SticTextSecondary)
                Text(text = "• Problemas con la impresora", fontSize = 13.sp, color = SticTextSecondary)
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onOpenDetail: (Long) -> Unit,
    onConfirmExternal: () -> Unit,
    onCancelExternal: () -> Unit,
    showActionButtons: Boolean = false,
    onCreateDraft: (GeneratedIncidentDraft) -> Unit = {}
) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bgColor = if (message.isUser) SticBlue else SticSky
    val txtColor = if (message.isUser) SticWhite else SticTextPrimary
    val shape = if (message.isUser) RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp) else RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Surface(color = bgColor, shape = shape, modifier = Modifier.widthIn(max = 300.dp)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = message.text,
                    color = txtColor,
                    fontSize = 14.sp
                )

                if (showActionButtons) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onConfirmExternal,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SticBlue)
                        ) {
                            Text("Buscar solución externa con Gemini", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onCancelExternal,
                            modifier = Modifier.fillMaxWidth(),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SticBlue)
                        ) {
                            Text("Mantener solo local", fontSize = 12.sp, color = SticBlue)
                        }
                    }
                }

                if (!message.isUser && message.usedExternalAi) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Esta respuesta procede de información externa. Revísala antes de guardarla.",
                        fontSize = 11.sp,
                        color = Color.Red.copy(alpha = 0.7f),
                        lineHeight = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { 
                            // Reconstruimos el borrador si no está presente en el mensaje persistido
                            val draft = message.generatedIncidentDraft ?: GeneratedIncidentDraft(
                                originalQuestion = "Consulta sobre: ${message.text.take(30)}...",
                                generatedAnswer = message.text,
                                suggestedTitle = "Solución sugerida por Gemini"
                            )
                            onCreateDraft(draft)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SticSky),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = SticBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Crear ficha provisional", fontSize = 12.sp, color = SticBlue)
                    }
                }
            }
        }

        if (!message.isUser) {
            ChatbotResponseMetadata(message)

            if (message.relatedIncidents.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (message.mainIncidentId != null) "Ficha recomendada:" else "Fichas relacionadas:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SticTextSecondary,
                    modifier = Modifier.padding(start = 4.dp)
                )
                
                // Mostrar la ficha principal destacada si existe
                val mainIncident = message.relatedIncidents.find { it.incidenciaId == message.mainIncidentId }
                val otherIncidents = message.relatedIncidents.filter { it.incidenciaId != message.mainIncidentId }

                if (mainIncident != null) {
                    ChatbotIncidentResultCard(mainIncident, onOpenDetail, isMain = true)
                    if (otherIncidents.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Otras alternativas:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SticTextSecondary, modifier = Modifier.padding(start = 4.dp))
                    }
                }

                otherIncidents.forEach { result ->
                    Spacer(modifier = Modifier.height(4.dp))
                    ChatbotIncidentResultCard(result, onOpenDetail)
                }
            }
        }
    }
}

@Composable
private fun ChatbotResponseMetadata(message: ChatMessage) {
    Column(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val icon = if (message.usedExternalAi) Icons.Default.Shield else Icons.Default.Info
            Icon(icon, contentDescription = null, modifier = Modifier.size(12.dp), tint = SticTextSecondary)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (message.usedExternalAi) "Generado con Gemini" else "Modo local",
                fontSize = 10.sp,
                color = SticTextSecondary,
                fontStyle = FontStyle.Italic
            )
        }
        
        if (message.confidence != null) {
            val confidenceColor = when(message.confidence) {
                "Alta" -> Color(0xFF2E7D32)
                "Media" -> Color(0xFFF57C00)
                else -> Color(0xFFC62828)
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(12.dp), tint = confidenceColor)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Confianza: ${message.confidence}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = confidenceColor
                )
            }
        }
    }
}

@Composable
fun ChatbotIncidentResultCard(
    result: ChatbotIncidentResult,
    onOpenIncidentDetail: (Long) -> Unit,
    isMain: Boolean = false
) {
    Card(
        modifier = Modifier.width(300.dp).padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = if (isMain) Color(0xFFF0F7FF) else SticWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isMain) 4.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isMain) SticBlue else SticSky)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isMain) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = SticBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = result.tituloNombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = SticBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (!result.motivoCoincidencia.isNullOrBlank()) {
                Text(text = "Motivo: ${result.motivoCoincidencia}", fontSize = 10.sp, color = SticTextSecondary, fontStyle = FontStyle.Italic)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = result.categoria, fontSize = 11.sp, color = SticTextSecondary)
                Text(text = " • ", fontSize = 11.sp, color = SticTextSecondary)
                Text(text = result.nivelPrioridad, fontSize = 11.sp, color = if (result.nivelPrioridad == "Urgente") Color.Red else SticTextSecondary)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = result.procedimientoResumen, style = MaterialTheme.typography.bodySmall, color = SticTextPrimary, maxLines = 2)
            
            TextButton(
                onClick = { onOpenIncidentDetail(result.incidenciaId) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(32.dp).align(Alignment.End)
            ) {
                Text("Ver detalle", fontSize = 12.sp, color = SticBlue)
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = SticBlue, strokeWidth = 2.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Gemini está analizando las fichas...", color = SticTextSecondary, fontSize = 12.sp)
    }
}
