package com.example.rentify_proyecto_intermodular.ui.incidents_tenant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard

@Composable
fun IncidentsTenantScreen(modifier: Modifier = Modifier, actualUser: User) {

    var incidencias by remember { mutableStateOf<MutableList<Incident>>(mutableListOf()) }

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
        Text(
            text = stringResource(R.string.incidents_tenant_slogan, actualUser.firstName),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* Si la cantidad de incidencias es 0, mostrar "CREAR INCIDENCIA"
            * Si hay una incidencia (o mas) activa, mostrar informacion acerca
            * de ella, ademas de poder editarla (editar asunto, editar descripcion) */

            if (incidencias.isNullOrEmpty()) {

                CommonCard(
                    title = stringResource(R.string.incidents_create_button),
                    expanded = false,
                    icon = null
                ) {
                    // TODO DARIUS: PONER AQUÍ EL CONTENIDO DEL ANIMATEDVISIBILITY (NO EL PROPIO ANIMATEDVISIBILITY) (SON LOS TEXT FIELDS PARA INSERTAR UNA INCIDENCIA)
                }
            } else {
                CommonCard(
                    title = "Broken Microwave", // TODO mostrar incident.issue
                    expanded = false,
                    icon = null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            /* todo MOSTRAR LA (INCIDENT.DESCRIPTION) EN ESTE TEXT */
                            text = "Hi, my microwave is not microwaving anymore, pls help!"
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                onClick = {
                                    /* todo DELETE DE LA INCIDENCIA SELECCIONADA DE LA BBDD. */
                                }
                            ) {
                                Text(
                                    text = "Delete incident"
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                onClick = {
                                    /* todo UPDATE DE LA INCIDENCIA SELECCIONADA DE LA BBDD. */
                                }
                            ) {
                                Text(
                                    text = "Edit incident"
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                onClick = {
                                    /* Este click permite resolver la incidencia PENDIENTE DE PENSAR */
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null
                                    )
                                    Text(
                                        text = "Resolve incident"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}