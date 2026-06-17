package com.example.sticbrain.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.R

/**
 * Paleta de colores Stic Brain.
 */
val SticBlue = Color(0xFF0D47A1)
val SticBlueLight = Color(0xFF1976D2)
val SticSky = Color(0xFFE3F2FD)
val SticWhite = Color(0xFFFFFFFF)
val SticBackground = Color(0xFFF5F9FF)
val SticSurface = Color(0xFFFFFFFF)
val SticTextPrimary = Color(0xFF102A43)
val SticTextSecondary = Color(0xFF627D98)
val SticBorder = Color(0xFFD9EAF7)
val SticGreen = Color(0xFF2E7D32)
val SticOrange = Color(0xFFF57C00)
val SticRed = Color(0xFFD32F2F)

/**
 * Cabecera superior para todas las pantallas.
 */
@Composable
fun SticTopHeader(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = SticBlue,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showBackButton) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = SticWhite)
                        }
                    }
                    Column {
                        Text(
                            text = title,
                            color = SticWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                color = SticSky.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    actions()
                }
            }
        }
    }
}

/**
 * Card base para container de información.
 */
@Composable
fun SticCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = SticSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, SticBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * Chip para filtros y etiquetas.
 */
@Composable
fun SticChip(
    text: String,
    containerColor: Color = SticSky,
    labelColor: Color = SticBlue,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = { onClick?.invoke() },
        color = if (isSelected) SticBlue else containerColor,
        shape = RoundedCornerShape(8.dp),
        border = if (!isSelected) BorderStroke(1.dp, SticBorder) else null
    ) {
        Text(
            text = text,
            color = if (isSelected) SticWhite else labelColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

/**
 * Etiqueta de prioridad con colores semánticos.
 */
@Composable
fun PriorityBadge(priority: String) {
    val (color, backgroundColor) = when (priority.uppercase()) {
        "CRÍTICA" -> SticRed to SticRed.copy(alpha = 0.1f)
        "ALTA" -> SticOrange to SticOrange.copy(alpha = 0.1f)
        "BAJA" -> SticGreen to SticGreen.copy(alpha = 0.1f)
        "NORMAL" -> SticBlueLight to SticBlueLight.copy(alpha = 0.1f)
        else -> SticBlue to SticSky
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Text(
            text = priority.uppercase(),
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

/**
 * Título de sección con icono opcional.
 */
@Composable
fun SectionTitle(
    text: String,
    icon: ImageVector? = null,
    color: Color = SticBlue
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Campo de texto estilizado para formularios.
 */
@Composable
fun SticTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            color = SticTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = SticTextSecondary, fontSize = 14.sp) },
            isError = isError,
            singleLine = singleLine,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SticWhite,
                unfocusedContainerColor = SticWhite,
                focusedBorderColor = SticBlue,
                unfocusedBorderColor = SticBorder,
                errorBorderColor = SticRed
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = SticRed,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

/**
 * Barra de navegación inferior común.
 */
@Composable
fun SticBottomBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNewIncidentClick: () -> Unit = {},
    onSupportClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = SticWhite,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.inicio)) },
            label = { Text(stringResource(R.string.inicio)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SticBlue,
                selectedTextColor = SticBlue,
                unselectedIconColor = SticTextSecondary,
                unselectedTextColor = SticTextSecondary,
                indicatorColor = SticSky
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = onSearchClick,
            icon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.buscar)) },
            label = { Text(stringResource(R.string.buscar)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SticBlue,
                selectedTextColor = SticBlue,
                unselectedIconColor = SticTextSecondary,
                unselectedTextColor = SticTextSecondary,
                indicatorColor = SticSky
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = onNewIncidentClick,
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = stringResource(R.string.nueva)) },
            label = { Text(stringResource(R.string.nueva)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SticBlue,
                selectedTextColor = SticBlue,
                unselectedIconColor = SticTextSecondary,
                unselectedTextColor = SticTextSecondary,
                indicatorColor = SticSky
            )
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = onSupportClick,
            icon = { Icon(Icons.Default.SupportAgent, contentDescription = stringResource(R.string.soporte)) },
            label = { Text(stringResource(R.string.soporte)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SticBlue,
                selectedTextColor = SticBlue,
                unselectedIconColor = SticTextSecondary,
                unselectedTextColor = SticTextSecondary,
                indicatorColor = SticSky
            )
        )
    }
}
