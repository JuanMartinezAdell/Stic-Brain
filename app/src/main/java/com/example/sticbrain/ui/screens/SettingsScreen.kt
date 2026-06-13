package com.example.sticbrain.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.ui.theme.*
import com.example.sticbrain.viewmodel.ImportUiState

@Composable
fun SettingsScreen(
    importUiState: ImportUiState,
    onImportExcelFile: (Uri) -> Unit = {},
    onResetImportState: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToProviders: () -> Unit = {},
    onExportData: () -> Unit = {},
    onBackupDatabase: () -> Unit = {},
    onRestoreDatabase: () -> Unit = {},
    onChatbotSettings: () -> Unit = {},
    onClearDemoData: () -> Unit = {}
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { onImportExcelFile(it) }
        }
    )

    Scaffold(
        containerColor = SticBackground,
        topBar = {
            SticTopHeader(
                title = "Ajustes y utilidades",
                subtitle = "Configuración y mantenimiento de Stic Brain",
                showBackButton = true,
                onBackClick = onNavigateBack
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // MOSTRAR ESTADO DE IMPORTACIÓN
            if (importUiState.isImporting) {
                ImportingCard()
            } else if (importUiState.result != null || importUiState.errorMessage != null) {
                ImportResultCard(importUiState, onResetImportState)
            }

            Text(
                text = "Administra la base de conocimiento, categorías, soporte y futuras funciones de IA.",
                color = SticTextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // SECCIÓN: BASE DE CONOCIMIENTO
            SettingsSection(title = "Base de conocimiento") {
                SettingsOptionItem(
                    icon = Icons.Default.Category,
                    title = "Gestionar categorías",
                    onClick = onNavigateToCategories
                )
                SettingsOptionItem(
                    icon = Icons.Default.UploadFile,
                    title = "Importar fichas desde Excel",
                    subtitle = "Carga una plantilla .xlsx con las fichas de conocimiento.",
                    onClick = {
                        launcher.launch(arrayOf(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            "application/vnd.ms-excel"
                        ))
                    }
                )
                SettingsOptionItem(
                    icon = Icons.Default.FileDownload,
                    title = "Exportar base de conocimiento",
                    subtitle = "Próximamente",
                    onClick = onExportData
                )
                SettingsOptionItem(
                    icon = Icons.Default.Backup,
                    title = "Copia de seguridad",
                    subtitle = "Próximamente",
                    onClick = onBackupDatabase
                )
                SettingsOptionItem(
                    icon = Icons.Default.Restore,
                    title = "Restaurar copia",
                    subtitle = "Próximamente",
                    onClick = onRestoreDatabase
                )
            }

            // SECCIÓN: SOPORTE Y ESCALADO
            SettingsSection(title = "Soporte y escalado") {
                SettingsOptionItem(
                    icon = Icons.Default.Business,
                    title = "Gestionar soporte y proveedores",
                    onClick = onNavigateToProviders
                )
                SettingsOptionItem(
                    icon = Icons.Default.SupportAgent,
                    title = "Ver servicios de soporte",
                    onClick = onNavigateToSupport
                )
            }

            // SECCIÓN: CHATBOT
            SettingsSection(title = "Chatbot técnico") {
                SettingsOptionItem(
                    icon = Icons.Default.SmartToy,
                    title = "Configurar chatbot",
                    subtitle = "Próximamente",
                    onClick = onChatbotSettings
                )
            }

            // SECCIÓN: DATOS DE PRUEBA
            SettingsSection(title = "Datos de prueba") {
                SettingsOptionItem(
                    icon = Icons.Default.DeleteSweep,
                    title = "Limpiar datos de prueba",
                    titleColor = SticRed,
                    onClick = onClearDemoData
                )
            }

            AppInfoSection()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ImportingCard() {
    SticCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = SticBlue, strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Importando archivo Excel...", color = SticBlue, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ImportResultCard(state: ImportUiState, onDismiss: () -> Unit) {
    SticCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.errorMessage != null) "Error en importación" else "Importación finalizada",
                    color = if (state.errorMessage != null) SticRed else SticGreen,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, tint = SticTextSecondary)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (state.errorMessage != null) {
                Text(text = state.errorMessage, color = SticTextPrimary, fontSize = 14.sp)
            } else if (state.result != null) {
                Text(text = "Total filas procesadas: ${state.result.totalFilas}", fontSize = 13.sp)
                Text(text = "Filas válidas importadas: ${state.result.filasValidas}", color = SticGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "Filas con errores: ${state.result.filasInvalidas}", color = SticRed, fontSize = 13.sp)
                
                if (state.result.errores.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Detalle de errores:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        state.result.errores.take(3).forEach { error ->
                            Text(text = "• $error", fontSize = 11.sp, color = SticTextSecondary)
                        }
                        if (state.result.errores.size > 3) {
                            Text(text = "... y ${state.result.errores.size - 3} errores más", fontSize = 11.sp, color = SticTextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title.uppercase(),
            color = SticBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        SticCard {
            content()
        }
    }
}

@Composable
private fun SettingsOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = SticTextPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (titleColor == SticRed) SticRed else SticBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = titleColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = SticTextSecondary,
                    fontSize = 12.sp
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = SticBorder,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun AppInfoSection() {
    SettingsSection(title = "Información") {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            AppInfoRow(label = "Nombre", value = "Stic Brain")
            AppInfoRow(label = "Tipo", value = "Base de conocimiento TIC")
            AppInfoRow(label = "Versión", value = "1.0")
            AppInfoRow(label = "Tecnologías", value = "Kotlin, Compose, Room")
        }
    }
}

@Composable
private fun AppInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = SticTextSecondary, fontSize = 13.sp)
        Text(text = value, color = SticTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SticBrainTheme {
        SettingsScreen(importUiState = ImportUiState())
    }
}
