package com.example.rentify_proyecto_intermodular

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentify_proyecto_intermodular.data.api.getPropertiesByOwner
import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard
import com.example.rentify_proyecto_intermodular.ui.home_owner.HomeOwnerScreen
import com.example.rentify_proyecto_intermodular.ui.login.LoginScreen
import com.example.rentify_proyecto_intermodular.ui.main.MainScreen
import com.example.rentify_proyecto_intermodular.ui.register.RegisterScreen
import com.example.rentify_proyecto_intermodular.ui.theme.RentifyProyectoIntermodularTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentifyProyectoIntermodularTheme {
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()
                val context = LocalContext.current

                val refreshPropertiesUnexpectedErrorMessage = stringResource(R.string.home_owner_refresh_properties_unexpected_error)

                var actualUser by rememberSaveable { mutableStateOf<User?>(null) }

                val onRefreshUserProperties: ()->Unit = {
                    val oldUser = actualUser

                    if (oldUser != null) {
                        coroutineScope.launch {
                            try {
                                val newProperties = getPropertiesByOwner(oldUser.id)
                                actualUser = oldUser.copy(
                                    ownedProperty = newProperties
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, refreshPropertiesUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else {
                        Toast.makeText(context, refreshPropertiesUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "Login"
                ) {
                    composable ("Login") {
                        LoginScreen(
                            applicationContext = applicationContext,
                            coroutineScope = coroutineScope,
                            navController = navController,
                            actualUser = actualUser,
                            onUserChange  = { actualUser = it }
                        )
                    }

                    composable ("Register") {
                        RegisterScreen(
                            applicationContext = applicationContext,
                            coroutineScope = coroutineScope,
                            navController = navController
                        )
                    }

                    composable ("Main") {
                        if (actualUser == null) {
                            Text(
                                text = "THERE WAS AN UNEXPECTED ERROR WHEN LOGGING IN. RETRY AGAIN OR CONTACT YOUR MANAGER.",
                            )
                        }
                        else {
                            MainScreen(
                                actualUser = actualUser!!,
                                onUserLogout = {
                                    navController.navigate("Login")
                                    actualUser = null
                                },
                                onUserUpdate = { user ->
                                    actualUser = user
                                },
                                onRefreshUserProperties = onRefreshUserProperties
                            )
                        }
                    }
                }
            }
        }
    }
}
