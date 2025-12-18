package com.example.rentify_proyecto_intermodular.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import  androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.login
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun LoginScreen(
    modifier: Modifier,
    applicationContext: Context,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    actualUser: User?,
    onUserChange: (User?) -> Unit
) {
    val radioButtons = listOf(
        LoginOption(
            R.string.radio_owner_login
        ),
        LoginOption(
            R.string.radio_tenant_login
        )
    )

    var selectedOption by remember { mutableStateOf(radioButtons[0]) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val registerRoute = stringResource(R.string.navigation_register)
    val homeOwnerRoute = stringResource(R.string.navigation_home_owner)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
                Text(
                    text = stringResource(R.string.app_slogan_login),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Card(
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .padding(
                        top = 40.dp,
                        bottom = 20.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    radioButtons.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable { selectedOption = option }
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = { selectedOption = option }
                            )
                            Text(
                                text = stringResource(option.text),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            Card(
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .padding(15.dp)
            ) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.scrim,
                        unfocusedTextColor = MaterialTheme.colorScheme.scrim,

                        focusedLabelColor = MaterialTheme.colorScheme.scrim,
                        unfocusedLabelColor = MaterialTheme.colorScheme.scrim,

                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,

                        focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedBorderColor = MaterialTheme.colorScheme.scrim,

                        cursorColor = MaterialTheme.colorScheme.scrim,
                    ),
                    modifier = Modifier
                        .padding(20.dp),
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.email_label_textfield)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.scrim,
                        unfocusedTextColor = MaterialTheme.colorScheme.scrim,

                        focusedLabelColor = MaterialTheme.colorScheme.scrim,
                        unfocusedLabelColor = MaterialTheme.colorScheme.scrim,

                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,

                        focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedBorderColor = MaterialTheme.colorScheme.scrim,

                        cursorColor = MaterialTheme.colorScheme.scrim,
                    ),
                    modifier = Modifier
                        .padding(20.dp),
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.password_label_textfield),
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                        )
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) {
                                        R.drawable.ic_visibility_on
                                    } else {
                                        R.drawable.ic_visibility_off
                                    }
                                ),
                                contentDescription = if (passwordVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                },
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .padding(5.dp),
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    if (selectedOption.text == R.string.radio_owner_login){
                                        val user = login(email, password, "owner")
                                        if (user == null){
                                            Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_LONG).show()
                                        }else{
                                            onUserChange(user)
                                            navController.navigate(homeOwnerRoute)
                                        }
                                    }
                                    else if (selectedOption.text == R.string.radio_tenant_login) {
                                        val user = login(email, password, "tenant")
                                        if (user == null){
                                            Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_LONG).show()
                                        }else{
                                            onUserChange(user)
                                            /*TODO PABLO implement navigation to tenant's home*/
                                            Toast.makeText(applicationContext, "Tenant login yet to be implemented", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                } catch (e: IOException) {
                                    Toast.makeText(applicationContext, "An Unexpected Error Ocurred. Try again.", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.login_label_button),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Text(
                        text = stringResource(R.string.signup_clickable_text),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .clickable(
                                onClick = { navController.navigate(registerRoute) }
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                }
            }
    }

}