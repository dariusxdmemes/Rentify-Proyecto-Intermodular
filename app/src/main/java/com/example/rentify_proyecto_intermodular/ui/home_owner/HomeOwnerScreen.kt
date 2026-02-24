package com.example.rentify_proyecto_intermodular.ui.home_owner

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.rentify_proyecto_intermodular.data.api.bindTenantToProperty
import com.example.rentify_proyecto_intermodular.data.api.deleteProperty
import com.example.rentify_proyecto_intermodular.data.api.getServicesByProperty
import com.example.rentify_proyecto_intermodular.data.api.getTenantsByProperty
import com.example.rentify_proyecto_intermodular.data.api.registerProperty
import com.example.rentify_proyecto_intermodular.data.api.updateProperty
import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonButton
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard
import com.example.rentify_proyecto_intermodular.ui.common.CommonDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeOwnerScreen(
    modifier: Modifier,
    actualUser: User,
    onRefreshUserProperties: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val invalidPriceFormatMessage = stringResource(R.string.home_owner_invalid_price_format)

    val createPropertySuccessMessage = stringResource(R.string.home_owner_create_property_success)
    val createPropertyUnexpectedErrorMessage = stringResource(R.string.home_owner_create_property_unexpected_error)

    val updatePropertySuccessMessage = stringResource(R.string.home_owner_update_property_success)
    val updatePropertyUnexpectedErrorMessage = stringResource(R.string.home_owner_update_property_unexpected_error)

    val deletePropertySuccessMessage = stringResource(R.string.home_owner_delete_property_success)
    val deletePropertyUnexpectedErrorMessage = stringResource(R.string.home_owner_delete_property_unexpected_error)

    val addTenantSuccessMessage = stringResource(R.string.home_owner_add_tenant_success)
    val addTenantUnexpectedErrorMessage = stringResource(R.string.home_owner_add_tenant_unexpected_error)
    val addTenantNotFoundOrAlreadyRegisteredMessage = stringResource(R.string.home_owner_add_tenant_not_found_or_already_added)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.scrim
        )
        Text(
            text = stringResource(R.string.home_owner_slogan, actualUser.firstName),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 40.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(actualUser.ownedProperty ?: emptyList()) { property ->
                var tenants by remember { mutableStateOf<List<User>>(emptyList()) }
                var services by remember { mutableStateOf<Service?>(null) }
                var isLoading by remember { mutableStateOf(false) }

                LaunchedEffect(property.id) {
                    isLoading = true
                    try {
                        coroutineScope {
                            val tenantsDeferred = async { getTenantsByProperty(property.id) }
                            val servicesDeferred = async { getServicesByProperty(property.id) }

                            tenants = tenantsDeferred.await()
                            services = servicesDeferred.await()
                        }
                    } finally {
                        isLoading = false
                    }
                }


                CommonCard(
                    title = "${property.address}, ${property.ciudad}, ${property.pais}",
                    icon = Icons.Default.Home,
                    expanded = false,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        var showUpdateDialog by remember { mutableStateOf(false) }
                        var showDeleteDialog by remember { mutableStateOf(false) }
                        var showAddTenantDialog by remember { mutableStateOf(false) }

                        if (isLoading) {
                            Text(text = "Cargando datos...")
                        } else {
                            if (tenants.isEmpty()) {
                                Text(text = "Inquilino: Sin inquilinos")
                            } else {
                                tenants.forEach { tenant ->
                                    Text(text = "Inquilino: ${tenant.firstName} ${tenant.lastName}")
                                }
                            }

                            Text(text = "Precio/mes: ${property.alquiler}")
                            Text(text = "Servicios: ${services?.included ?: "No disponible"}")
                            Text(text = "No incluye: ${services?.excluded ?: "No disponible"}")
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CommonButton(
                                text = stringResource(R.string.home_owner_update_property),
                                onClick = {
                                    showUpdateDialog = !showUpdateDialog
                                }
                            )
                            CommonButton(
                                text = stringResource(R.string.home_owner_delete_property),
                                onClick = {
                                    showDeleteDialog = !showDeleteDialog
                                }
                            )
                            CommonButton(
                             text = stringResource(R.string.home_owner_add_tenant),
                                onClick = {
                                    showAddTenantDialog = !showAddTenantDialog
                                }
                            )
                        }

                        //Dialog to delete a property
                        if (showDeleteDialog)
                            CommonDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                dialogTitle = stringResource(R.string.home_owner_delete_property_dialog_title),
                                dialogText = stringResource(R.string.home_owner_delete_property_confirmation_message),
                                icon = null,
                                content = {},
                                onConfirmation = {
                                    coroutineScope.launch {
                                        try {
                                            deleteProperty(property.id)
                                            Toast.makeText(context, deletePropertySuccessMessage, Toast.LENGTH_LONG).show()
                                        }
                                        catch (e: Exception){
                                            Toast.makeText(context, deletePropertyUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                        }
                                        showDeleteDialog = false
                                        onRefreshUserProperties()
                                    }
                                }
                            )

                        var actualPrice by remember(property.alquiler) { mutableStateOf(property.alquiler.toString()) }

                        var actualServices by remember(services) {
                            mutableStateOf(services?.included ?: "Nothing to see...")
                        }
                        var actualExcludedServices by remember(services) {
                            mutableStateOf(services?.excluded ?: "Nothing to see...")
                        }

                        // Dialog to update a property
                        if (showUpdateDialog) {
                            CommonDialog(
                                onDismissRequest = { showUpdateDialog = !showUpdateDialog },
                                onConfirmation = {
                                    coroutineScope.launch {
                                        val actualPriceInt = actualPrice.toIntOrNull()
                                        if (actualPriceInt == null || actualPriceInt <= 0){
                                            Toast.makeText(context, invalidPriceFormatMessage, Toast.LENGTH_LONG).show()
                                            return@launch
                                        }

                                        try {
                                            updateProperty(
                                                Property(
                                                    id = property.id,
                                                    address = property.address,
                                                    owner_fk = property.owner_fk,
                                                    ciudad = property.ciudad,
                                                    pais = property.pais,
                                                    alquiler = actualPriceInt
                                                )
                                            )

                                            Toast.makeText(context, updatePropertySuccessMessage, Toast.LENGTH_LONG).show()
                                            showUpdateDialog = false
                                        }
                                        catch (e: Exception){
                                            Toast.makeText(context, updatePropertyUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                        }

                                        onRefreshUserProperties()
                                    }
                                },
                                dialogTitle = stringResource(R.string.home_owner_update_property),
                                dialogText = "",
                                icon = null
                            ) {
                                Surface {
                                    Column(
                                        modifier = Modifier.padding(top = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = actualPrice,
                                            onValueChange = { actualPrice = it },
                                            label = { Text("Price") },
                                            placeholder = { Text("new price") },
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        OutlinedTextField(
                                            value = actualServices,
                                            onValueChange = { actualServices = it },
                                            label = { Text("actualServices") },
                                            placeholder = { Text("NewServices") },
                                            modifier = Modifier.fillMaxWidth(),
                                            minLines = 3
                                        )

                                        OutlinedTextField(
                                            value = actualExcludedServices,
                                            onValueChange = { actualExcludedServices = it },
                                            label = { Text("Excluded services") },
                                            placeholder = { Text("new excluded services") },
                                            modifier = Modifier.fillMaxWidth(),
                                            minLines = 3
                                        )
                                    }
                                }
                            }
                        }

                        var newTenantEmail by remember { mutableStateOf("") }

                        // Dialog to add a tenant to the property
                        if (showAddTenantDialog) {
                            CommonDialog(
                                onDismissRequest = { showAddTenantDialog = !showAddTenantDialog },
                                onConfirmation = {
                                    coroutineScope.launch {
                                        try {
                                            val code = bindTenantToProperty(property.id, newTenantEmail)

                                            when (code) {
                                                0 -> Toast.makeText(context, addTenantSuccessMessage, Toast.LENGTH_LONG).show()
                                                1 -> Toast.makeText(context, addTenantNotFoundOrAlreadyRegisteredMessage, Toast.LENGTH_LONG).show()
                                                else -> Toast.makeText(context, addTenantUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                        catch (e: Exception){
                                            Toast.makeText(context, deletePropertyUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                                        }

                                        showAddTenantDialog = false
                                        onRefreshUserProperties()
                                    }
                                },
                                dialogTitle = stringResource(R.string.home_owner_add_tenant),
                                dialogText = "texto",
                                icon = null
                            ) {
                                Surface {
                                    Column(
                                        modifier = Modifier.padding(top = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = newTenantEmail,
                                            onValueChange = { newTenantEmail = it },
                                            label = { Text("Tenant email") },
                                            placeholder = { Text("email@example.com") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var showCreateDialog by remember { mutableStateOf(false) }

        var propertyPrice by remember { mutableStateOf("") }
        var propertyServices by remember { mutableStateOf("") }
        var propertyExcludedServices by remember { mutableStateOf("") }
        var propertyAddress by remember { mutableStateOf("") }
        var propertyCity by remember { mutableStateOf("") }
        var propertyCountry by remember { mutableStateOf("") }

        // Button to create a property
        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.home_owner_create_property),
            onClick = { showCreateDialog = !showCreateDialog }
        )

        // Dialog to create a property
        if (showCreateDialog) {
            CommonDialog(
                onDismissRequest = { showCreateDialog = !showCreateDialog },
                onConfirmation = {
                    coroutineScope.launch {
                        val propertyPriceInt = propertyPrice.toIntOrNull()

                        if (propertyPriceInt == null || propertyPriceInt <= 0){
                            Toast.makeText(context, invalidPriceFormatMessage, Toast.LENGTH_LONG).show()
                            return@launch
                        }

                        try {
                            registerProperty(
                                Property(
                                    id = 0,
                                    address = propertyAddress,
                                    owner_fk = actualUser.id,
                                    ciudad = propertyCity,
                                    pais = propertyCountry,
                                    alquiler = propertyPriceInt
                                )
                            )

                            Toast.makeText(context, createPropertySuccessMessage, Toast.LENGTH_LONG).show()
                        }
                        catch (e: Exception){
                            Toast.makeText(context, createPropertyUnexpectedErrorMessage, Toast.LENGTH_LONG).show()
                        }

                        showCreateDialog = false
                        onRefreshUserProperties()
                    }
                },
                dialogTitle = stringResource(R.string.home_owner_create_property),
                dialogText = "texto",
                icon = null
            ) {
                Surface {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        OutlinedTextField(
                            value = propertyPrice,
                            onValueChange = { propertyPrice = it },
                            label = { Text("Property price") },
                            placeholder = { Text("Whats the property's price?") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = propertyServices,
                            onValueChange = { propertyServices = it },
                            label = { Text("Included services") },
                            placeholder = { Text("Which services are included?") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = propertyExcludedServices,
                            onValueChange = { propertyExcludedServices = it },
                            label = { Text("Excluded services") },
                            placeholder = { Text("Which services are not included?") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = propertyAddress,
                            onValueChange = { propertyAddress = it },
                            label = { Text("Property address") },
                            placeholder = { Text("Ex. Calle Flores, 33") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = propertyCity,
                            onValueChange = { propertyCity = it },
                            label = { Text("Property city") },
                            placeholder = { Text("Ex. Valencia") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = propertyCountry,
                            onValueChange = { propertyCountry = it },
                            label = { Text("Property country") },
                            placeholder = { Text("Ex. Spain") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}