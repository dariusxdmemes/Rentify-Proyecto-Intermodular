package com.example.rentify_proyecto_intermodular.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

@Composable
fun RegisterScreen(modifier: Modifier) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val registerFields = listOf(
        RegisterOption(
            stringResource(R.string.register_first_name_label),
            firstName,
            KeyboardOptions(keyboardType = KeyboardType.Text)
        ),
        RegisterOption(
            stringResource(R.string.register_last_name_label),
            lastName,
            KeyboardOptions(keyboardType = KeyboardType.Text)
        ),
        RegisterOption(
            stringResource(R.string.register_phone_label),
            phoneNumber,
            KeyboardOptions(keyboardType = KeyboardType.Number)
        ),
        RegisterOption(
            stringResource(R.string.register_email_label),
            email,
            KeyboardOptions(keyboardType = KeyboardType.Email)
        ),
        RegisterOption(
            stringResource(R.string.register_passwd_label),
            password,
            KeyboardOptions(keyboardType = KeyboardType.Password)
        ),
        RegisterOption(
            stringResource(R.string.register_confirm_passwd_label),
            confirmPassword,
            KeyboardOptions(keyboardType = KeyboardType.Password)
        )

    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
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
                            value = field.fieldValue,
                            onValueChange = {
                                field.fieldValue = it
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

                    // TODO implement Register button
                }
            }
        }
    }
}