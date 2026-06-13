package com.example.sticbrain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sticbrain.R

@Composable
fun IncidentCard(
    severity: String,
    severityColor: Color,
    title: String,
    tag: String,
    tagColor: Color,
    subtitle: String,
    status: String,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = Color(0xFF161B22),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    color = severityColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = severity,
                        color = severityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF8B949E),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = subtitle,
                    color = Color(0xFF8B949E),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = date,
                    color = Color(0xFF8B949E),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedItem: Int = 0,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNewIncidentClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color(0xFF0D1117),
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.inicio)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5865F2),
                selectedTextColor = Color(0xFF5865F2),
                unselectedIconColor = Color(0xFF8B949E),
                unselectedTextColor = Color(0xFF8B949E),
                indicatorColor = Color(0xFF5865F2).copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = onSearchClick,
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text(stringResource(R.string.buscar)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5865F2),
                selectedTextColor = Color(0xFF5865F2),
                unselectedIconColor = Color(0xFF8B949E),
                unselectedTextColor = Color(0xFF8B949E),
                indicatorColor = Color(0xFF5865F2).copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = onNewIncidentClick,
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
            label = { Text(stringResource(R.string.nueva)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5865F2),
                selectedTextColor = Color(0xFF5865F2),
                unselectedIconColor = Color(0xFF8B949E),
                unselectedTextColor = Color(0xFF8B949E),
                indicatorColor = Color(0xFF5865F2).copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = onSupportClick,
            icon = { Icon(Icons.Default.SupportAgent, contentDescription = null) },
            label = { Text(stringResource(R.string.soporte)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF5865F2),
                selectedTextColor = Color(0xFF5865F2),
                unselectedIconColor = Color(0xFF8B949E),
                unselectedTextColor = Color(0xFF8B949E),
                indicatorColor = Color(0xFF5865F2).copy(alpha = 0.1f)
            )
        )
    }
}
