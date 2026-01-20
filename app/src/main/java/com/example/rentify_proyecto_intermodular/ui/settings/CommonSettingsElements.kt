package com.example.rentify_proyecto_intermodular.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.updateUser
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsCard(title: String, content: @Composable ()->Unit){
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text(text = title) // TITLE <-------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }

                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        content() // CONTENT <-------------------------
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateAccountInfoCard(
    actualUser: User,
    context: Context,
    onUserUpdate: (User)->Unit
){
    val coroutineScope = rememberCoroutineScope()

    SettingsCard(
        title = stringResource(R.string.settings_update_card)
    ) {
        var firstName by remember { mutableStateOf(actualUser.firstName) }
        var lastName by remember { mutableStateOf(actualUser.lastName) }
        var phoneNumber by remember { mutableStateOf(actualUser.phoneNumber) }
        var email by remember { mutableStateOf(actualUser.email) }
        var newPassword by remember { mutableStateOf("") }
        var confirmNewPassword by remember { mutableStateOf("") }
        var oldPassword by remember { mutableStateOf("") }

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = stringResource(R.string.register_first_name_label)) }
        )
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = stringResource(R.string.register_last_name_label)) }
        )
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text = stringResource(R.string.register_phone_label)) }
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(R.string.register_email_label)) }
        )
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text(text = stringResource(R.string.settings_new_password)) }
        )
        TextField(
            value = confirmNewPassword,
            onValueChange = { confirmNewPassword = it },
            label = { Text(text = stringResource(R.string.settings_confirm_new_password)) }
        )
        TextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text(text = stringResource(R.string.settings_old_password)) }
        )

        Button(
            onClick = {
                if (newPassword == confirmNewPassword){
                    coroutineScope.launch {
                        try {
                            val newUser = updateUser(
                                user = User (
                                    actualUser.id,
                                    firstName,
                                    lastName,
                                    phoneNumber,
                                    email,
                                    oldPassword
                                ),
                                actualpassword = oldPassword,
                                newpassword = newPassword
                            )

                            if (newUser == null) {
                                Toast.makeText(context, "Ha habido un error inesperado", Toast.LENGTH_LONG).show()
                            }
                            else {
                                Toast.makeText(context, "Usuario actualizado correctamente!", Toast.LENGTH_LONG).show()
                                onUserUpdate(newUser)
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else {
                    Toast.makeText(context, "New passwords don't match!", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            Text(
                text = stringResource(R.string.settings_update_button)
            )
        }
    }
}

@Composable
fun LogoutCard(onUserLogout: () -> Unit) {
    SettingsCard(
        title = stringResource(R.string.settings_logout_card)
    ) {
        Text(
            text = "By pressing the button below, you will logout of your account. You will be redirected to login screen. You will have to login again to keep using the app"
        )
        Button(
            onClick = onUserLogout
        ){
            Text(text = stringResource(R.string.settings_logout_button))
        }
    }
}

@Composable
fun DeleteAccountCard(){
    SettingsCard(
        title = stringResource(R.string.settings_delete_account_card)
    ) {
        Text(
            text = stringResource(R.string.settings_delete_account_text)
        )
        Button(
            onClick = {
                // TODO implement delete account
            }
        ){
            Text(text = stringResource(R.string.settings_delete_account_button))
        }
    }
}
