package com.example.rentify_proyecto_intermodular.ui.incidents_owner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.getIncidentsByProperty
import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.User

@Composable
fun IncidentsOwnerScreen(modifier: Modifier = Modifier, actualUser: User) {

    var incidents by remember { mutableStateOf<List<Incident>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var onError by remember { mutableStateOf(false) }

    /* Aqui se almacenan los datos recogidos de getIncidentsPropertyById()
    * Y, como no hay viewModel, los estados de la pantalla se almacenan en
    * la propia vista, se manejan las distintas "vistas", loading, onError y
    * el propio request 200, es decir el data fetch correcto. */

    LaunchedEffect(actualUser.id) {
        try {
            isLoading = true
            onError = false

            val allIncidents = mutableListOf<Incident>()
            val properties = actualUser.ownedProperty ?: emptyList()

            if (properties.isNotEmpty()) {
                properties.forEach { property ->
                    val fetchedIncidents = getIncidentsByProperty(property.id)
                    allIncidents.addAll(fetchedIncidents)
                }
                incidents = allIncidents
            } else {
                incidents = emptyList()
            }
        } catch (e: Exception) {
            onError = true
        } finally {
            isLoading = false
        }
    }

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

        when {
            isLoading -> {
                /* todo IMPLEMENT A CIRCULAR LOADING ANIMATION "CIRCULAR_PROGRESS_INDICATOR" */
                Text(
                    text = stringResource(R.string.incidents_owner_loading_label)
                )
            }
            onError -> {
                Text(
                    text = stringResource(R.string.incidents_owner_on_error_req)
                )
                /* todo IMPLEMENT A "REFRESH DATA" BUTTON SO USER DOESNT NEED TO RE-OPEN THE APP */
            }
            incidents.isNullOrEmpty() -> {
                EmptyIncidentsScreen()
            }
            else -> {
                /* Este es el "happy path", si los datos no se muestran puede ser por:

                * - La api no esta arrancada (no se puede hacer el fetch).
                * - El usuario no tiene ninguna incidencia. (no hay datos en la tabla). */

                Text(
                    text = stringResource(R.string.incidents_owner_pending_slogan),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                )
                incidents!!.forEach { incident ->
                    var expanded by remember { mutableStateOf(false) }
                    Card(
                        onClick = {
                            expanded = !expanded
                        },
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
                                .padding(10.dp)
                        ) {
                            Text(
                                text = incident.tenant?.firstName ?: stringResource(R.string.incidents_owner_tenant_name_ph)
                            )
                            Text(
                                text = incident.tenant?.lastName ?: stringResource(R.string.incidents_owner_tenant_surname_ph)
                            )
                            Text(
                                text = incident.issue
                            )
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
                                        text = incident.issue
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = stringResource(R.string.incidents_owner_descripition_label),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = incident.description
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyIncidentsScreen() {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
                text = stringResource(R.string.incidents_owner_default_slogan),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.incidents_is_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}