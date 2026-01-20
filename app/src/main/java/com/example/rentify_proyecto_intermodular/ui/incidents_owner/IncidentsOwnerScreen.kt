package com.example.rentify_proyecto_intermodular.ui.incidents_owner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.model.User

@Composable
fun IncidentsOwnerScreen(modifier: Modifier = Modifier, actualUser: User) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.scrim
        )

        /* Al terminar las dos pantallas añadir dos caminos para mostrar los text
        * 1 - Si el propietario tiene incidencias mostrar incidents_pending_slogan
        * 2 - Si el propietario no tiene ninguna inciendia se muestra incidents_default_slogan */

        Text(
            text = stringResource(R.string.incidents_owner_default_slogan),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(R.string.incidents_owner_pending_slogan),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.scrim
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.scrim
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable(
                            onClick = {
                                expanded = !expanded
                                /* Desplegar la información de la incidencia */
                            }
                        )
                ) {
                    Text(
                        text = "John Doe, "
                    )
                    Text(
                        text = "Calle Falsa, "
                    )
                    Text(
                        text = "123"
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(
                        text = "Jane Doe, "
                    )
                    Text(
                        text = "Calle Verdadera, "
                    )
                    Text(
                        text = "321"
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.incidents_owner_issue_label),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 5.dp, end = 5.dp),
                            text = "Lorem ipsum dolor"
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.incidents_owner_descripition_label),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet glorius ave caesar servita sitae"
                    )

                }
            }
        }
    }
}