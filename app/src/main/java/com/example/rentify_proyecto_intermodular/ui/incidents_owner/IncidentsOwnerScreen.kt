package com.example.rentify_proyecto_intermodular.ui.incidents_owner

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.getServicesByProperty
import com.example.rentify_proyecto_intermodular.data.api.getIncidentsByProperty
import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(actualUser.ownedProperty ?: emptyList()) { property ->
                PropertyIncidentsCard(property = property)

            }
        }
    }
}

@Composable
fun PropertyIncidentsCard(property: Property) {
    var incidents by remember { mutableStateOf<List<Incident>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(property.id) {
        try {
            incidents = getIncidentsByProperty(property.id)
        } catch (e: Exception) {
            Log.e("IncidentsOwner", "Error al cargar incidencias para la propiedad ${property.id}: ${e.message}")
        } finally {
            isLoading = false
        }
    }

        if (isLoading) {
            Text(
                text = "Cargando incidencias para ${property.address}...",
                modifier = Modifier.padding(16.dp)
            )
        } else if (incidents.isEmpty()) {
            Text(
                text = "No hay incidencias para ${property.address}",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column {
                incidents.forEach { incident ->
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
                    ){
                        IncidentItem(incident = incident, property = property)
                    }
                }
            }
        }

}


@Composable
fun IncidentItem(incident: Incident, property: Property) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(5.dp)
        ) {
            Text(text = incident.tenant?.firstName.orEmpty())
            Text(text = incident.tenant?.lastName.orEmpty())
            Text(text = property.address)
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
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
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
        )

}