package com.example.rentify_proyecto_intermodular.ui.incidents_tenant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonButton
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard
import com.example.rentify_proyecto_intermodular.ui.common.CommonDialog

@Composable
fun IncidentsTenantScreen(modifier: Modifier = Modifier, actualUser: User) {

    var incidencias by remember { mutableStateOf<MutableList<Incident>>(mutableListOf()) }
    incidencias.addAll(
        listOf(
            Incident(
                30,
                "Faucet leaking",
                "My faucet started leaking water 3 days ago",
                1,
                User(
                    30,
                    "Paco",
                    "Fiestas",
                    "666666666",
                    "fiestas.paco@gmx.com",
                    "1234"
                ),
                1
            ),
            Incident(
                31,
                "Black mold on ceiling",
                "My celing has grown black mold",
                1,
                User(
                    31,
                    "Pablo",
                    "Porras",
                    "677777777",
                    "pablo.porras@gmx.com",
                    "1234"
                ),
                1
            )
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
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

        if (incidencias.isNullOrEmpty()) {

            CommonCard(
                title = stringResource(R.string.incidents_create_button),
                expanded = false,
                icon = null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    var issuePlaceholder by remember { mutableStateOf("") }
                    var descriptionPlaceholder by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = issuePlaceholder,
                        onValueChange = {
                            issuePlaceholder = it
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.incidents_owner_issue_label)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,

                            focusedLabelColor = MaterialTheme.colorScheme.scrim,
                            unfocusedLabelColor = MaterialTheme.colorScheme.scrim,

                            focusedPlaceholderColor = MaterialTheme.colorScheme.scrim,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.scrim,

                            focusedTextColor = MaterialTheme.colorScheme.scrim,
                            unfocusedTextColor = MaterialTheme.colorScheme.scrim,

                            cursorColor = MaterialTheme.colorScheme.scrim
                        )
                    )
                    OutlinedTextField(
                        value = descriptionPlaceholder,
                        onValueChange = {
                            descriptionPlaceholder = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        minLines = 1,
                        maxLines = 2,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.incidents_owner_descripition_label)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,

                            focusedLabelColor = MaterialTheme.colorScheme.scrim,
                            unfocusedLabelColor = MaterialTheme.colorScheme.scrim,

                            focusedPlaceholderColor = MaterialTheme.colorScheme.scrim,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.scrim,

                            focusedTextColor = MaterialTheme.colorScheme.scrim,
                            unfocusedTextColor = MaterialTheme.colorScheme.scrim,

                            cursorColor = MaterialTheme.colorScheme.scrim
                        )
                    )
                    ElevatedButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {  }
                    ) {
                        Text(
                            text = stringResource(R.string.incidents_create_button)
                        )
                    }
                }
            }
            Spacer(modifier.weight(1f))
        }
        else {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /* Si la cantidad de incidencias es 0, mostrar "CREAR INCIDENCIA"
            * Si hay una incidencia (o mas) activa, mostrar informacion acerca
            * de ella, ademas de poder editarla (editar asunto, editar descripcion) */

                items (incidencias) { incident ->
                    CommonCard(
                        title = incident.issue,
                        expanded = false,
                        icon = null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                        ) {

                            var showDialog by remember { mutableStateOf(false) }

                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                text = incident.description
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    modifier = Modifier
                                        .padding(top = 5.dp, bottom = 5.dp),
                                    onClick = {
                                        showDialog = !showDialog
                                    }
                                ) {
                                    Text(
                                        text = stringResource(R.string.incidents_edit_incident_button_title)
                                    )
                                }

                                var newIssue by remember { mutableStateOf("") }
                                var newDescription by remember { mutableStateOf("") }

                                if (showDialog) {
                                    CommonDialog(
                                        onDismissRequest = { showDialog = !showDialog },
                                        onConfirmation = { /* todo CONFIRMAR UPDATE INCIDENT.ISSUE + INCIDENT.DESCRIP. */ },
                                        dialogTitle = stringResource(R.string.incidents_edit_incident_button_title),
                                        dialogText = "texto",
                                        icon = null
                                    ) {
                                        Surface {
                                            Column(
                                                modifier = Modifier.padding(top = 16.dp),
                                                verticalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                OutlinedTextField(
                                                    value = newIssue,
                                                    onValueChange = { newIssue = it },
                                                    label = { Text("Issue") },
                                                    placeholder = { Text("new issue") },
                                                    modifier = Modifier.fillMaxWidth()
                                                )

                                                OutlinedTextField(
                                                    value = newDescription,
                                                    onValueChange = { newDescription = it },
                                                    label = { Text("Description") },
                                                    placeholder = { Text("new description") },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    minLines = 3
                                                )
                                            }
                                        }
                                    }
                                }
                                Button(
                                    modifier = Modifier
                                        .padding(top = 5.dp, bottom = 5.dp),
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
                                            text = stringResource(R.string.incidents_resolve_incident_button)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.incidents_new_incident_fab_title),
            onClick = {/*TODO create an incident*/}
        )
    }
}