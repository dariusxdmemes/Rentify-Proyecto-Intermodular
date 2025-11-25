package com.example.rentify_proyecto_intermodular.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import  androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R

@Composable
fun LoginScreen(modifier: Modifier) {

    val radioButtons = listOf(
        LoginOption(
            R.string.radio_owner_login
        ),
        LoginOption(
            R.string.radio_tenant_login
        )
    )
    var selectedOption by remember { mutableStateOf(radioButtons[0]) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name)
                )
                Text(
                    text = stringResource(R.string.app_slogan_login)
                )
            }

            Card(
                modifier = Modifier
                // TODO:  Implementar el card con los dos tipos de login.
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    radioButtons.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { selectedOption = option }
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = { selectedOption = option }
                            )
                            Text(
                                text = stringResource(option.text)
                            )
                        }
                    }
                }
            }
        }
    }

}