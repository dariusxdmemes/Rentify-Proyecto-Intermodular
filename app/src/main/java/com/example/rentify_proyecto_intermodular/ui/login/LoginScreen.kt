package com.example.rentify_proyecto_intermodular.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle

@Composable
fun LoginScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Login Works!",
            style = MaterialTheme.typography.titleLarge
        )
    }

}