package com.example.rentify_proyecto_intermodular.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destinations(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String,
    val label: String
) {
    HOME("Home", Icons.Default.Home, "Home", "Home"),
    INCIDENTS("Incidents", Icons.Default.Build, "Incidents", "Incidents"),
    DOCUMENTS("Documents", Icons.Default.MailOutline, "Documents", "Documents"),
    OPTIONS("Options", Icons.Default.Settings, "Options", "Options")
}
