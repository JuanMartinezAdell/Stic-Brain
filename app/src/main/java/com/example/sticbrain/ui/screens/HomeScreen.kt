package com.example.sticbrain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.R
import com.example.sticbrain.ui.components.BottomNavigationBar
import com.example.sticbrain.ui.components.IncidentCard
import com.example.sticbrain.ui.theme.SticBrainTheme

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToNewIncident: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToIncidentDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0D1117),
        bottomBar = {
            BottomNavigationBar(
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
            HeaderSection()
            StatsSection()
            FilterSection()
            IncidentsListSection(onIncidentClick = onNavigateToIncidentDetail)
        }
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF21262D), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color(0xFF8B949E),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.kbase_tic),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.base_conocimientos),
                    color = Color(0xFF8B949E),
                    fontSize = 12.sp
                )
            }
        }
        Surface(
            color = Color(0xFF161B22),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.online),
                color = Color(0xFF3FB950),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun StatsSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            count = "8",
            label = stringResource(R.string.incidencias),
            countColor = Color(0xFF58A6FF),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = "8",
            label = stringResource(R.string.resueltas),
            countColor = Color(0xFF3FB950),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = "2",
            label = stringResource(R.string.criticas),
            countColor = Color(0xFFF85149),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = "6",
            label = stringResource(R.string.proveedores),
            countColor = Color(0xFF388BFD),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    count: String,
    label: String,
    countColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF161B22),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                color = countColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color(0xFF8B949E),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun FilterSection(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { FilterChip(text = stringResource(R.string.todas), selected = true) }
        item { FilterChip(text = stringResource(R.string.red), textColor = Color(0xFF58A6FF)) }
        item { FilterChip(text = stringResource(R.string.his), textColor = Color(0xFFA371F7)) }
        item { FilterChip(text = stringResource(R.string.pacs), textColor = Color(0xFF388BFD)) }
        item { FilterChip(text = stringResource(R.string.impresoras), textColor = Color(0xFFDB6D28)) }
        item { FilterChip(text = stringResource(R.string.correo), textColor = Color(0xFFD29922)) }
    }
}

@Composable
fun FilterChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    textColor: Color = Color.White
) {
    Surface(
        color = if (selected) Color(0xFF5865F2) else Color(0xFF161B22),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else textColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun IncidentsListSection(
    onIncidentClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "8 incidencias",
                color = Color(0xFF8B949E),
                fontSize = 12.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = Color(0xFF8B949E),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = stringResource(R.string.mas_recientes),
                    color = Color(0xFF8B949E),
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                IncidentCard(
                    severity = stringResource(R.string.critica),
                    severityColor = Color(0xFFF85149),
                    title = "Usuarios no pueden acceder a Soaris tras actualización",
                    tag = stringResource(R.string.his),
                    tagColor = Color(0xFFA371F7),
                    subtitle = "Soaris HIS",
                    status = stringResource(R.string.resuelta),
                    date = "2024-11-15",
                    onClick = { onIncidentClick(1L) }
                )
            }
            item {
                IncidentCard(
                    severity = stringResource(R.string.alta),
                    severityColor = Color(0xFFDB6D28),
                    title = "PACS sin acceso a imágenes desde Urgencias",
                    tag = stringResource(R.string.pacs),
                    tagColor = Color(0xFF388BFD),
                    subtitle = "Sectra PACS",
                    status = stringResource(R.string.resuelta),
                    date = "2024-11-08",
                    onClick = { onIncidentClick(2L) }
                )
            }
            item {
                IncidentCard(
                    severity = stringResource(R.string.media),
                    severityColor = Color(0xFFD29922),
                    title = "VPN Pulse Secure desconecta a los 5 minutos",
                    tag = stringResource(R.string.vpn),
                    tagColor = Color(0xFF3FB950),
                    subtitle = "FortiGate SSL-VPN",
                    status = stringResource(R.string.resuelta),
                    date = "2024-10-22",
                    onClick = { onIncidentClick(3L) }
                )
            }
            item {
                IncidentCard(
                    severity = stringResource(R.string.alta),
                    severityColor = Color(0xFFDB6D28),
                    title = "Impresoras de planta no imprimen etiquetas de paciente",
                    tag = stringResource(R.string.impresoras),
                    tagColor = Color(0xFFDB6D28),
                    subtitle = "Zebra ZD421 / Admisión HIS",
                    status = stringResource(R.string.resuelta),
                    date = "2024-10-15",
                    onClick = { onIncidentClick(4L) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SticBrainTheme(darkTheme = true) {
        HomeScreen(
            onNavigateToSearch = {},
            onNavigateToNewIncident = {},
            onNavigateToSupport = {},
            onNavigateToIncidentDetail = {}
        )
    }
}
