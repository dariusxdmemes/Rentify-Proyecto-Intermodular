package com.example.rentify_proyecto_intermodular.ui.register

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.registerUser
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@Composable
fun RegisterScreen(
    modifier: Modifier,
    applicationContext: Context,
    coroutineScope: CoroutineScope
) {

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
                        Button(
                            onClick = {
                                if (registerFields[4].fieldState.value != registerFields[5].fieldState.value) // check if both password fields match
                                    Toast.makeText(applicationContext, "Passwords don't match", Toast.LENGTH_LONG).show()
                                else
                                    coroutineScope.launch{
                                        try {
                                            val statusCode = registerUser(
                                                user = User(
                                                    0,
                                                    registerFields[0].fieldState.value,
                                                    registerFields[1].fieldState.value,
                                                    registerFields[2].fieldState.value,
                                                    registerFields[3].fieldState.value,
                                                    registerFields[4].fieldState.value
                                                )
                                            )
                                            Toast.makeText(applicationContext, statusCode, Toast.LENGTH_LONG).show()


                                        } catch (e: Exception) {
                                            Toast.makeText(applicationContext, "Unexpected error. Try again later.", Toast.LENGTH_LONG).show()
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