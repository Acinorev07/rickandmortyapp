package com.example.rickandmortyapp.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rickandmortyapp.R

enum class DrawerItem(
    val icon: ImageVector,
    val resourceId: Int
) {
    ABOUT(Icons.Default.Info, R.string.formulario),
    SETTINGS(Icons.Default.Settings, R.string.configuración),
    RECENT(Icons.Default.DateRange, R.string.recientes),
    UPLOAD(Icons.Default.KeyboardArrowDown, R.string.descargas)

}