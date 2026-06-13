package com.example.sticbrain.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.ui.components.BottomNavigationBar
import com.example.sticbrain.ui.theme.SticBrainTheme

@Composable
fun IncidentDetailScreen(
    incidenciaId: Long,
    onNavigateBack: () -> Unit = {},
    onDeleteIncident: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFF0D1117),
        topBar = {
            IncidentDetailHeader(
                incidenciaId = incidenciaId,
                onNavigateBack = onNavigateBack,
                onDeleteIncident = onDeleteIncident
            )
        },
        bottomBar = {
            IncidentDetailBottomBar(
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
            IncidentMainInfo()
            Spacer(modifier = Modifier.height(24.dp))
            DescriptionSection()
            Spacer(modifier = Modifier.height(24.dp))
            ErrorMessageSection()
            Spacer(modifier = Modifier.height(24.dp))
            RootCauseSection()
            Spacer(modifier = Modifier.height(24.dp))
            ResolutionSection()
            Spacer(modifier = Modifier.height(24.dp))
            KeywordsSection()
            Spacer(modifier = Modifier.height(24.dp))
            EscalationSection()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun IncidentDetailHeader(
    incidenciaId: Long,
    onNavigateBack: () -> Unit,
    onDeleteIncident: () -> Unit
) {
    // Datos temporales
    val incidentIdLabel = "#I$incidenciaId"
    val environment = "Producción"
    val severity = "CRÍTICA"
    val severityColor = Color(0xFFF85149)

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF58A6FF)
                    )
                }
                Column {
                    Text(
                        text = "DETALLE DE INCIDENCIA",
                        color = Color(0xFF8B949E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$incidentIdLabel · ",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = environment,
                            color = Color(0xFF8B949E),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = severityColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, severityColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = severity,
                        color = severityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDeleteIncident,
                    modifier = Modifier
                        .background(Color(0xFFF85149).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFF85149),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF30363D), thickness = 1.dp)
    }
}

@Composable
private fun IncidentMainInfo() {
    // Datos temporales
    val title = "Usuarios no pueden acceder a Soaris tras actualización"
    val tag = "HIS"
    val tagColor = Color(0xFFA371F7)
    val status = "Resuelta"
    val date = "2024-11-15"
    val application = "Soaris HIS"

    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = tagColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = tag,
                    color = tagColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF3FB950), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = status,
                    color = Color(0xFF3FB950),
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = date,
                color = Color(0xFF8B949E),
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Dns,
                contentDescription = null,
                tint = Color(0xFF8B949E),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = application,
                color = Color(0xFFC9D1D9),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun DescriptionSection() {
    // Datos temporales
    val description = "Tras la actualización de Soaris a v4.2.1 múltiples usuarios reportan error al autenticarse. El servicio de worklist no responde."

    Column {
        SectionTitle(text = "DESCRIPCIÓN", icon = Icons.Default.Info)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            color = Color(0xFFC9D1D9),
            fontSize = 15.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun ErrorMessageSection() {
    // Datos temporales
    val errorMessage = "ERROR_AUTH_0x00F4: SessionManager timeout after 30000ms"

    Column {
        SectionTitle(text = "MENSAJE DE ERROR", icon = Icons.Default.Warning, color = Color(0xFFF85149))
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            color = Color(0xFFF85149).copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFF85149).copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFF85149),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* Copiar */ }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar error",
                        tint = Color(0xFFF85149),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RootCauseSection() {
    // Datos temporales
    val rootCause = "El servicio SessionManager de Soaris quedó en estado inconsistente tras el reinicio post-actualización. La caché de sesiones no se limpió correctamente."

    Column {
        SectionTitle(text = "CAUSA RAÍZ", icon = Icons.Default.Psychology, color = Color(0xFFDB6D28))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = rootCause,
            color = Color(0xFFC9D1D9),
            fontSize = 15.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun ResolutionSection() {
    // Datos temporales
    val resolutionSteps = listOf(
        "Detener servicio SoarisSessionMgr desde Administrador de servicios de Windows.",
        "Eliminar contenido de C:\\Soaris\\cache\\sessions\\",
        "Reiniciar servicio SoarisSessionMgr.",
        "Verificar con un usuario piloto antes de comunicar resolución."
    )

    Column {
        SectionTitle(text = "RESOLUCIÓN", icon = Icons.Default.CheckCircleOutline, color = Color(0xFFA371F7))
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            color = Color(0xFF161B22),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF30363D))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { /* Copiar resolución */ }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar",
                            tint = Color(0xFF8B949E),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                resolutionSteps.forEachIndexed { index, step ->
                    Row(modifier = Modifier.padding(vertical = 6.dp)) {
                        Text(
                            text = "${index + 1}. ",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = step,
                            color = Color(0xFFC9D1D9),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KeywordsSection() {
    // Datos temporales
    val keywords = listOf("soaris", "autenticación", "sesión", "sessionmanager", "login", "timeout")

    Column {
        SectionTitle(text = "PALABRAS CLAVE", icon = Icons.Default.LocalOffer)
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            keywords.forEach { keyword ->
                Surface(
                    color = Color(0xFF21262D),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF30363D))
                ) {
                    Text(
                        text = keyword,
                        color = Color(0xFF8B949E),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EscalationSection() {
    // Datos temporales
    val providerInfo = """
        HIS · Soaris
        Tel.      900 102 345
        Email     soporte.his@siemens-healthineers.com
        Horario   L-V 08:00-20:00
        SLA       4h crítica / 8h alta
    """.trimIndent()

    Column {
        SectionTitle(text = "ESCALADO — SIEMENS HEALTHINEERS", icon = Icons.Default.PhoneInTalk, color = Color(0xFF58A6FF))
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            color = Color(0xFF161B22),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF30363D))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = providerInfo,
                    color = Color(0xFF8B949E),
                    fontSize = 13.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color = Color(0xFF8B949E)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun IncidentDetailBottomBar(
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNewIncidentClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    BottomNavigationBar(
        selectedItem = -1,
        onHomeClick = onHomeClick,
        onSearchClick = onSearchClick,
        onNewIncidentClick = onNewIncidentClick,
        onSupportClick = onSupportClick
    )
}

@Preview(showBackground = true)
@Composable
fun IncidentDetailScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        IncidentDetailScreen(incidenciaId = 1L)
    }
}
