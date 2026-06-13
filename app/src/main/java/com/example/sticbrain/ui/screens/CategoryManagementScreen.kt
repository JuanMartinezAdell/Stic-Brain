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
import com.example.sticbrain.data.local.entity.CategoriaEntity
import com.example.sticbrain.ui.theme.*

@Composable
fun CategoryManagementScreen(
    categorias: List<CategoriaEntity>,
    queryBusqueda: String = "",
    onQueryChange: (String) -> Unit = {},
    onCreateCategory: (CategoriaEntity) -> Unit = {},
    onUpdateCategory: (CategoriaEntity) -> Unit = {},
    onDeleteCategory: (CategoriaEntity) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNewIncident: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    var showFormDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<CategoriaEntity?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<CategoriaEntity?>(null) }

    Scaffold(
        containerColor = SticBackground,
        topBar = {
            SticTopHeader(
                title = "Mantenimiento",
                subtitle = "Categorías de la base de conocimiento",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            SticBottomBar(
                selectedItem = -1, // Pantalla de administración
                onHomeClick = onNavigateToHome,
                onSearchClick = onNavigateToSearch,
                onNewIncidentClick = onNavigateToNewIncident,
                onSupportClick = onNavigateToSupport
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedCategory = null
                    showFormDialog = true
                },
                containerColor = SticBlue,
                contentColor = SticWhite
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva categoría")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            CategorySearchField(
                query = queryBusqueda,
                onQueryChange = onQueryChange,
                count = categorias.size
            )

            if (categorias.isEmpty()) {
                CategoryEmptyState(onAddClick = {
                    selectedCategory = null
                    showFormDialog = true
                })
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categorias) { categoria ->
                        CategoryCard(
                            categoria = categoria,
                            onEdit = {
                                selectedCategory = categoria
                                showFormDialog = true
                            },
                            onToggleStatus = {
                                onUpdateCategory(categoria.copy(activa = !categoria.activa))
                            },
                            onDelete = {
                                showDeleteConfirm = categoria
                            }
                        )
                    }
                }
            }
        }

        if (showFormDialog) {
            CategoryFormDialog(
                categoria = selectedCategory,
                onDismiss = { showFormDialog = false },
                onConfirm = {
                    if (selectedCategory == null) onCreateCategory(it)
                    else onUpdateCategory(it)
                    showFormDialog = false
                }
            )
        }

        if (showDeleteConfirm != null) {
            DeleteCategoryDialog(
                categoria = showDeleteConfirm!!,
                onDismiss = { showDeleteConfirm = null },
                onConfirm = {
                    onDeleteCategory(it)
                    showDeleteConfirm = null
                }
            )
        }
    }
}

@Composable
private fun CategorySearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    count: Int
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar categoría...", color = SticTextSecondary, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = SticBlue) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, null)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SticWhite,
                unfocusedContainerColor = SticWhite,
                focusedBorderColor = SticBlue,
                unfocusedBorderColor = SticBorder
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$count categorías",
            color = SticTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun CategoryCard(
    categoria: CategoriaEntity,
    onEdit: () -> Unit,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    SticCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoria.nombre,
                    color = SticTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                SticChip(
                    text = if (categoria.activa) "Activa" else "Inactiva",
                    containerColor = if (categoria.activa) SticSky else Color.LightGray.copy(alpha = 0.2f),
                    labelColor = if (categoria.activa) SticBlue else Color.Gray
                )
            }
            
            if (!categoria.descripcion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = categoria.descripcion,
                    color = SticTextSecondary,
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp), tint = SticRed)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar", color = SticRed)
                }
                TextButton(onClick = onToggleStatus) {
                    Icon(
                        imageVector = if (categoria.activa) Icons.Default.Block else Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (categoria.activa) SticOrange else SticGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (categoria.activa) "Desactivar" else "Activar",
                        color = if (categoria.activa) SticOrange else SticGreen
                    )
                }
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp), tint = SticBlue)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", color = SticBlue)
                }
            }
        }
    }
}

@Composable
private fun CategoryFormDialog(
    categoria: CategoriaEntity?,
    onDismiss: () -> Unit,
    onConfirm: (CategoriaEntity) -> Unit
) {
    var nombre by remember { mutableStateOf(categoria?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(categoria?.descripcion ?: "") }
    var activa by remember { mutableStateOf(categoria?.activa ?: true) }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (categoria == null) "Nueva categoría" else "Editar categoría",
                color = SticBlue,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SticTextField(
                    value = nombre,
                    onValueChange = { 
                        nombre = it
                        error = false
                    },
                    label = "Nombre *",
                    placeholder = "Ej: Hardware",
                    isError = error,
                    errorMessage = "El nombre es obligatorio"
                )
                SticTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = "Descripción",
                    placeholder = "Breve descripción...",
                    singleLine = false,
                    modifier = Modifier.height(100.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = activa,
                        onCheckedChange = { activa = it },
                        colors = CheckboxDefaults.colors(checkedColor = SticBlue)
                    )
                    Text(text = "Categoría activa", color = SticTextPrimary)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank()) {
                        val entity = categoria?.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            activa = activa
                        ) ?: CategoriaEntity(
                            nombre = nombre,
                            descripcion = descripcion,
                            activa = activa
                        )
                        onConfirm(entity)
                    } else {
                        error = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SticBlue)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SticTextSecondary)
            }
        },
        containerColor = SticWhite,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun DeleteCategoryDialog(
    categoria: CategoriaEntity,
    onDismiss: () -> Unit,
    onConfirm: (CategoriaEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "¿Eliminar categoría?",
                color = SticRed,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Esta acción eliminará la categoría \"${categoria.nombre}\". Si ya está siendo usada por fichas de conocimiento, es recomendable desactivarla en lugar de eliminarla.",
                color = SticTextPrimary
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(categoria) },
                colors = ButtonDefaults.buttonColors(containerColor = SticRed)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SticTextSecondary)
            }
        },
        containerColor = SticWhite,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun CategoryEmptyState(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = SticBorder,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay categorías creadas",
                color = SticTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Añade una categoría para clasificar las fichas de conocimiento.",
                color = SticTextSecondary,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = SticBlue)
            ) {
                Text("Crear primera categoría")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryManagementScreenPreview() {
    CategoryManagementScreen(
        categorias = listOf(
            CategoriaEntity(id = 1, nombre = "Red y conectividad", activa = true),
            CategoriaEntity(id = 2, nombre = "Hardware", activa = false)
        )
    )
}
