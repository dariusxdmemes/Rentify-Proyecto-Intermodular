package com.example.rentify_proyecto_intermodular.ui.incidents_tenant

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.createIncident
import com.example.rentify_proyecto_intermodular.data.api.deleteIncident
import com.example.rentify_proyecto_intermodular.data.api.getIncidentsByProperty
import com.example.rentify_proyecto_intermodular.data.api.updateIncident
import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonButton
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard
import com.example.rentify_proyecto_intermodular.ui.common.CommonDialog
import kotlinx.coroutines.launch
import javax.sql.CommonDataSource

@Composable
fun IncidentsTenantScreen(
    modifier: Modifier = Modifier,
    actualUser: User
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var incidents by remember { mutableStateOf<List<Incident>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var onError by remember { mutableStateOf(false) }

    var showCreateIncidentDialog by remember { mutableStateOf(false) }

    val createDialogTitle = stringResource(R.string.incident_tenant_create_dialog_title)
    val updateDialogTitle = stringResource(R.string.incident_tenant_update_dialog_title)
    val deleteDialogTitle = stringResource(R.string.incident_tenant_delete_dialog_title)

    val createIncidentUnexpectedErrorMessage = stringResource(R.string.create_incidents_unexpected_error)
    val createIncidentSuccessMessage = stringResource(R.string.create_incident_success)
    val updateIncidentUnexpectedErrorMessage = stringResource(R.string.update_incidents_unexpected_error)
    val updateIncidentSuccessMessage = stringResource(R.string.update_incident_success)
    val deleteIncidentSuccessMessage = stringResource(R.string.delete_incident_success)
    val deleteIncidentUnexpectedErrorMessage = stringResource(R.string.delete_incidents_unexpected_error)

    var issuePlaceholder by remember { mutableStateOf("") }
    var descriptionPlaceholder by remember { mutableStateOf("") }

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

        if (actualUser.leasedProperty == null){
            Text(stringResource(R.string.home_tenant_no_property))
        }
        else {
            var refreshTrigger by remember {mutableStateOf(0)}
            LaunchedEffect(refreshTrigger) {
                try {
                    isLoading = true; onError = false
                    incidents = getIncidentsByProperty(actualUser.leasedProperty.id)
                } catch (e: Exception) {
                    onError = true
                } finally {
                    isLoading = false
                }
            }

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

                /* Si la cantidad de incidencias es 0, mostrar "CREAR INCIDENCIA"
                * Si hay una incidencia (o mas) activa, mostrar informacion acerca
                * de ella, ademas de poder editarla (editar asunto, editar descripcion) */

                incidents.isEmpty() -> {
                    // Card to create an incident (when there are no incidents)
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

                            // Button to create an incident (card)
                            ElevatedButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {
                                    coroutineScope.launch {
                                        try {
                                            createIncident(
                                                Incident(
                                                    id = 0,
                                                    issue = issuePlaceholder,
                                                    description = descriptionPlaceholder,
                                                    property_id = actualUser.leasedProperty.id,
                                                    tenant = actualUser,
                                                    owner_id = actualUser.leasedProperty.owner_fk
                                                )
                                            )
                                            Toast.makeText(context, createIncidentSuccessMessage, Toast.LENGTH_LONG).show()
                                        }
                                        catch (e: Exception) {
                                            Toast.makeText(context, createIncidentUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                        }

                                        showCreateIncidentDialog = false
                                        refreshTrigger++
                                        issuePlaceholder = ""
                                        descriptionPlaceholder = ""
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.incidents_create_button)
                                )
                            }
                        }
                    }
                    Spacer(modifier.weight(1f))
                }

                else -> {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(incidents) { incident ->
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
                                        var showUpdateDialog by remember { mutableStateOf(false) }

                                        // Button to update an incident
                                        Button(
                                            modifier = Modifier
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            onClick = {
                                                showUpdateDialog = !showUpdateDialog
                                            }
                                        ) {
                                            Text(
                                                text = stringResource(R.string.incidents_edit_incident_button_title)
                                            )
                                        }

                                        var newIssue by remember { mutableStateOf(incident.issue) }
                                        var newDescription by remember { mutableStateOf(incident.description) }

                                        // Dialog to update an incident
                                        if (showUpdateDialog) {
                                            CommonDialog(
                                                onDismissRequest = { showUpdateDialog = !showUpdateDialog },
                                                onConfirmation = {
                                                    coroutineScope.launch {
                                                        try{
                                                            updateIncident(
                                                                Incident(
                                                                    id = incident.id,
                                                                    issue = newIssue,
                                                                    description = newDescription,
                                                                    property_id = incident.property_id,
                                                                    tenant = incident.tenant,
                                                                    owner_id = incident.owner_id
                                                                )
                                                            )

                                                            Toast.makeText(context, updateIncidentSuccessMessage, Toast.LENGTH_LONG).show()
                                                        }
                                                        catch (e: Exception){
                                                            Toast.makeText(context, updateIncidentUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                                        }

                                                        showUpdateDialog = false
                                                        refreshTrigger++
                                                        newIssue = ""
                                                        newDescription = ""
                                                    }
                                                },
                                                dialogTitle = updateDialogTitle,
                                                dialogText = "texto",
                                                icon = null
                                            ) {
                                                Surface {
                                                    Column(
                                                        modifier = Modifier.padding(top = 16.dp),
                                                        verticalArrangement = Arrangement.spacedBy(
                                                            10.dp
                                                        )
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

                                        var showDeleteDialog by remember { mutableStateOf(false) }

                                        // Button to resolve incidents
                                        Button(
                                            modifier = Modifier
                                                .padding(top = 5.dp, bottom = 5.dp),
                                            onClick = {
                                                showDeleteDialog = true
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

                                        // Dialog to resolve incidents
                                        if (showDeleteDialog)
                                            CommonDialog(
                                                onDismissRequest = { showDeleteDialog = false },
                                                onConfirmation = {
                                                    coroutineScope.launch {
                                                        try {
                                                            deleteIncident(incident.id)
                                                            Toast.makeText(context, deleteIncidentSuccessMessage, Toast.LENGTH_LONG).show()
                                                        }
                                                        catch (e: Exception){
                                                            Toast.makeText(context, deleteIncidentUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                                        }

                                                        showDeleteDialog = false
                                                        refreshTrigger++
                                                    }
                                                },
                                                dialogTitle = deleteDialogTitle,
                                                dialogText = stringResource(R.string.incidents_resolve_incident_button),
                                                icon = null,
                                                content = {}
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Button to create an incident (bottom)
            CommonButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.incidents_new_incident_fab_title),
                onClick = {
                    showCreateIncidentDialog = true
                }
            )

            // Dialog to create an incident (bottom)
            if (showCreateIncidentDialog)
                CommonDialog(
                    onDismissRequest = { showCreateIncidentDialog = false },
                    onConfirmation = {
                        coroutineScope.launch {
                            try {
                                createIncident(
                                    Incident(
                                        id = 0,
                                        issue = issuePlaceholder,
                                        description = descriptionPlaceholder,
                                        property_id = actualUser.leasedProperty.id,
                                        tenant = actualUser,
                                        owner_id = actualUser.leasedProperty.owner_fk
                                    )
                                )
                                Toast.makeText(context, createIncidentSuccessMessage, Toast.LENGTH_LONG).show()
                            }
                            catch (e: Exception) {
                                Toast.makeText(context, createIncidentUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                            }

                            showCreateIncidentDialog = false
                            refreshTrigger++
                            issuePlaceholder = ""
                            descriptionPlaceholder = ""
                        }
                    },
                    dialogTitle = createDialogTitle,
                    dialogText = "",
                    icon = Icons.Default.Add
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    ) {
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
                    }
                }
        }
    }
}