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
import com.example.sticbrain.ui.components.*
import com.example.sticbrain.ui.theme.*
import com.example.sticbrain.viewmodel.ExportUiState
import com.example.sticbrain.viewmodel.ImportUiState

/**
 * Pantalla de Ajustes y Utilidades.
 * 
 * Centraliza las funciones administrativas de la aplicación, como la gestión
 * de categorías, importación/exportación de datos y herramientas de desarrollo.
 */
@Composable
fun SettingsScreen(
    importUiState: ImportUiState,
    exportUiState: ExportUiState,
    onImportExcelFile: (Uri) -> Unit = {},
    onExportExcelFile: (Uri) -> Unit = {},
    onResetImportState: () -> Unit = {},
    onResetExportState: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToProviders: () -> Unit = {},
    onBackupDatabase: () -> Unit = {},
    onRestoreDatabase: () -> Unit = {},
    onChatbotSettings: () -> Unit = {},
    onClearDemoData: () -> Unit = {}
) {
    // Selector para abrir archivo Excel
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> uri?.let { onImportExcelFile(it) } }
    )

    // Selector para guardar archivo Excel
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        onResult = { uri -> uri?.let { onExportExcelFile(it) } }
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
            // Paneles informativos de progreso (Importación / Exportación)
            if (importUiState.isImporting) {
                StatusCard(text = "Importando archivo Excel...", isLoading = true)
            } else if (importUiState.result != null || importUiState.errorMessage != null) {
                ImportResultCard(importUiState, onResetImportState)
            }

            if (exportUiState.isExporting) {
                StatusCard(text = "Exportando fichas a Excel...", isLoading = true)
            } else if (exportUiState.result != null || exportUiState.errorMessage != null) {
                ExportResultCard(exportUiState, onResetExportState)
            }

            Text(
                text = "Administra la base de conocimiento, categorías, soporte y futuras funciones de IA.",
                color = SticTextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // GRUPO: GESTIÓN DE DATOS
            SettingsSection(title = "Base de conocimiento") {
                SettingsOptionItem(
                    icon = Icons.Default.Category,
                    title = "Gestionar categorías",
                    onClick = onNavigateToCategories
                )
                SettingsOptionItem(
                    icon = Icons.Default.UploadFile,
                    title = "Importar fichas desde Excel",
                    subtitle = "Carga una plantilla .xlsx con datos masivos",
                    onClick = {
                        importLauncher.launch(arrayOf(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            "application/vnd.ms-excel"
                        ))
                    }
                )
                SettingsOptionItem(
                    icon = Icons.Default.FileDownload,
                    title = "Exportar base de conocimiento",
                    subtitle = "Genera un backup en formato Excel",
                    onClick = {
                        exportLauncher.launch("stic_brain_export.xlsx")
                    }
                )
                SettingsOptionItem(
                    icon = Icons.Default.Backup,
                    title = "Copia de seguridad",
                    subtitle = "Próximamente",
                    onClick = onBackupDatabase
                )
            }

            // GRUPO: SOPORTE
            SettingsSection(title = "Soporte y escalado") {
                SettingsOptionItem(
                    icon = Icons.Default.Business,
                    title = "Gestionar directorio de soporte",
                    onClick = onNavigateToProviders
                )
            }

            // GRUPO: INTELIGENCIA ARTIFICIAL (DISEÑO FUTURO)
            SettingsSection(title = "Chatbot técnico (Próximamente)") {
                SettingsOptionItem(
                    icon = Icons.Default.SmartToy,
                    title = "Configurar asistente IA",
                    onClick = onChatbotSettings
                )
            }

            // GRUPO: HERRAMIENTAS TÉCNICAS
            SettingsSection(title = "Herramientas") {
                SettingsOptionItem(
                    icon = Icons.Default.DeleteSweep,
                    title = "Limpiar datos de prueba",
                    titleColor = SticRed,
                    onClick = onClearDemoData
                )
            }

            // Información técnica de la versión
            AppInfoSection()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/** Tarjeta para mostrar estados de espera con spinner. */
@Composable
private fun StatusCard(text: String, isLoading: Boolean) {
    SticCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = SticBlue, strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(text = text, color = SticBlue, fontWeight = FontWeight.Bold)
        }
    }
}

/** Muestra el resumen numérico de lo importado y los posibles errores. */
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
                Text(text = "Total procesadas: ${state.result.totalFilas}", fontSize = 13.sp)
                Text(text = "Válidas: ${state.result.filasValidas}", color = SticGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "Errores: ${state.result.filasInvalidas}", color = SticRed, fontSize = 13.sp)
            }
        }
    }
}

/** Muestra si la exportación a Excel ha tenido éxito. */
@Composable
private fun ExportResultCard(state: ExportUiState, onDismiss: () -> Unit) {
    SticCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.errorMessage != null) "Error al exportar" else "Exportación finalizada",
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
                Text(text = "Fichas exportadas: ${state.result.exportadas}", fontSize = 13.sp)
                Text(text = "Archivo generado correctamente", color = SticGreen, fontSize = 13.sp)
            }
        }
    }
}

/** Estructura visual para cada grupo de ajustes. */
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title.uppercase(),
            color = SticBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        SticCard {
            content()
        }
    }
}

/** Elemento clicable de la lista de ajustes. */
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
                fontSize = 15.sp,
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

/** Bloque con datos técnicos de la aplicación. */
@Composable
private fun AppInfoSection() {
    SettingsSection(title = "Información del sistema") {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            AppInfoRow(label = "Versión", value = "1.0.0 Stable")
            AppInfoRow(label = "Arquitectura", value = "MVVM + Compose")
            AppInfoRow(label = "Base de datos", value = "SQLite / Room")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Stic Brain es una base de conocimiento optimizada para el entorno sanitario TIC.",
                color = SticTextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
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
    SettingsScreen(
        importUiState = ImportUiState(),
        exportUiState = ExportUiState()
    )
}
