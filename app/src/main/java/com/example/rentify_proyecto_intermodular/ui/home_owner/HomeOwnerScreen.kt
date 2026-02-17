package com.example.rentify_proyecto_intermodular.ui.home_owner

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
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.getServicesByProperty
import com.example.rentify_proyecto_intermodular.data.api.getTenantsByProperty
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonButton
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Composable
fun HomeOwnerScreen(
    modifier: Modifier,
    actualUser: User?
) {
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
            text = stringResource(R.string.home_owner_slogan, actualUser?.firstName.toString()),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 40.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(actualUser?.ownedProperty ?: emptyList()) { property ->
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
                                onClick = {/* TODO update property*/}
                            )

                            CommonButton(
                                text = stringResource(R.string.home_owner_delete_property),
                                onClick = {/* TODO delete property*/}
                            )
                        }
                    }
                }
            }
        }

        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.home_owner_create_property),
            onClick = {/*TODO create a property*/}
        )
    }
}
