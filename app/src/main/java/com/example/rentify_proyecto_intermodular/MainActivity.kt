package com.example.rentify_proyecto_intermodular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.rentify_proyecto_intermodular.ui.home_owner.HomeOwnerScreen
import com.example.rentify_proyecto_intermodular.ui.login.LoginScreen
import com.example.rentify_proyecto_intermodular.ui.register.RegisterScreen
import com.example.rentify_proyecto_intermodular.ui.theme.RentifyProyectoIntermodularTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentifyProyectoIntermodularTheme {
                val coroutineScope = rememberCoroutineScope()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        applicationContext = applicationContext,
                        coroutineScope = coroutineScope
                    )
//                    RegisterScreen(
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .background(MaterialTheme.colorScheme.primaryContainer),
//                        applicationContext = applicationContext,
//                        coroutineScope = coroutineScope
//                    )
//                    HomeOwnerScreen(
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .background(MaterialTheme.colorScheme.primaryContainer)
//                    )
                }
            }
        }
    }
}
