package com.example.rentify_proyecto_intermodular.ui.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.registerUser
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    applicationContext: Context,
    coroutineScope: CoroutineScope,
    navController: NavHostController
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        val modifier = Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.primaryContainer)

        val loginRoute = stringResource(R.string.navigation_login)

        val registerFields = listOf(
            RegisterOption(
                stringResource(R.string.register_first_name_label),
                remember { mutableStateOf("") },
                KeyboardOptions(keyboardType = KeyboardType.Text)
            ),
            RegisterOption(
                stringResource(R.string.register_last_name_label),
                remember { mutableStateOf("") },
                KeyboardOptions(keyboardType = KeyboardType.Text)
            ),
            RegisterOption(
                stringResource(R.string.register_phone_label),
                remember { mutableStateOf("") },
                KeyboardOptions(keyboardType = KeyboardType.Number)
            ),
            RegisterOption(
                stringResource(R.string.register_email_label),
                remember { mutableStateOf("") },
                KeyboardOptions(keyboardType = KeyboardType.Email)
            ),
            RegisterOption(
                stringResource(R.string.register_passwd_label),
                remember { mutableStateOf("") },
                KeyboardOptions(keyboardType = KeyboardType.Password)
            ),
            RegisterOption(
                stringResource(R.string.register_confirm_passwd_label),
                remember { mutableStateOf("") },
                KeyboardOptions(keyboardType = KeyboardType.Password)
            )

        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
                Text(
                    text = stringResource(R.string.app_slogan_register),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        registerFields.forEach { field ->
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
                                value = field.fieldState.value,
                                onValueChange = {
                                    field.fieldState.value = it
                                },
                                label = {
                                    Text(
                                        text = field.fieldLabel
                                    )
                                },
                                keyboardOptions = field.fieldKeyboardOption,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val allFieldsRequiredMessage = stringResource(R.string.register_all_fields_required)
                            val invalidEmailFormatMessage = stringResource(R.string.register_invalid_email_format)
                            val invalidPhoneFormatMessage = stringResource(R.string.register_invalid_phone_format)
                            val invalidPasswordFormatMessage = stringResource(R.string.register_invalid_password_format)
                            val passwordsDoNotMatchMessage = stringResource(R.string.passwords_do_not_match)
                            val registerSuccessfulMessage = stringResource(R.string.register_success)
                            val userAlreadyExistsMessage = stringResource(R.string.register_user_already_exists)
                            val unexpectedErrorMessage = stringResource(R.string.register_unexpected_error)

                            Button(
                                onClick = {

                                    val firstName = registerFields[0].fieldState.value
                                    val lastName = registerFields[1].fieldState.value
                                    val phoneNumber = registerFields[2].fieldState.value
                                    val email = registerFields[3].fieldState.value
                                    val password = registerFields[4].fieldState.value
                                    val confirmPassord = registerFields[5].fieldState.value

                                    when {
                                        firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank() || email.isBlank() || password.isBlank() -> {
                                            Toast.makeText(applicationContext, allFieldsRequiredMessage,
                                                Toast.LENGTH_LONG).show()
                                        }

                                        !isValidEmail(email) -> {
                                            Toast.makeText(applicationContext, invalidEmailFormatMessage,
                                                Toast.LENGTH_LONG).show()
                                        }

                                        !isValidPhone(phoneNumber) -> {
                                            Toast.makeText(applicationContext, invalidPhoneFormatMessage, Toast.LENGTH_SHORT).show()
                                        }

                                        !isValidPassword(password) -> {
                                            Toast.makeText(applicationContext, invalidPasswordFormatMessage,
                                                Toast.LENGTH_LONG).show()
                                        }

                                        password != confirmPassord -> {
                                            Toast.makeText(applicationContext, passwordsDoNotMatchMessage, Toast.LENGTH_LONG).show()
                                        }

                                        else -> {
                                            coroutineScope.launch {
                                                try {
                                                    val statusCode = registerUser(
                                                        user = User(
                                                            0,
                                                            firstName,
                                                            lastName,
                                                            phoneNumber,
                                                            email,
                                                            password
                                                        )
                                                    )

                                                    when(statusCode) {
                                                        0 -> {
                                                            Toast.makeText(applicationContext, registerSuccessfulMessage, Toast.LENGTH_SHORT).show()
                                                            navController.navigate(loginRoute)
                                                        }
                                                        1 -> Toast.makeText(applicationContext, userAlreadyExistsMessage, Toast.LENGTH_LONG).show()
                                                        2 -> Toast.makeText(applicationContext, unexpectedErrorMessage,
                                                            Toast.LENGTH_SHORT).show()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(applicationContext, unexpectedErrorMessage,
                                                        Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.register_button_text),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}