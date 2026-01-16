package com.example.rentify_proyecto_intermodular.ui.home_owner

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.getServicesByProperty
import com.example.rentify_proyecto_intermodular.data.api.getTenantsByProperty
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Composable
fun HomeOwnerScreen(
    modifier: Modifier,
    actualUser: User?
) {
    val user = actualUser

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.scrim
        )
        Text(
            text = stringResource(R.string.home_owner_slogan, user?.firstName.toString()),
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(user?.ownedProperty ?: emptyList()) { property ->
                var expanded by remember { mutableStateOf(false) }

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
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .scale(2f)
                        )
                        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                            Text(text = property.address)
                            Text(text = "${property.ciudad}, ${property.pais}")

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
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}