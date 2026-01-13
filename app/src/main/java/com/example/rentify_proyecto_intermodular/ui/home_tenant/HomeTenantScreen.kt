package com.example.rentify_proyecto_intermodular.ui.home_tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.getServicesByProperty
import com.example.rentify_proyecto_intermodular.data.api.getTenantsByProperty
import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User

@Composable
fun HomeTenantScreen(
    modifier: Modifier = Modifier,
    actualUser: User,
    leasedProperty: Property
) {
    var services by remember { mutableStateOf<Service?>(null) }
    LaunchedEffect(leasedProperty.id) {
        services = getServicesByProperty(leasedProperty.id)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.scrim
            )
            Text(
                text = stringResource(R.string.home_tenant_slogan, actualUser.firstName),
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            //todo [REPLACE ACTUAL USER DATA WITH OWNER DATA]
                            text = "${stringResource(R.string.home_tenant_name_placeholder)} ${actualUser.firstName} ${actualUser.lastName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${stringResource(R.string.home_tenant_adress_placeholder)} ${leasedProperty.address}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${stringResource(R.string.home_tenant_price)} ${leasedProperty.alquiler} ${stringResource(R.string.euro)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${stringResource(R.string.home_tenant_services)} ${services?.included}" ?: stringResource(R.string.home_tenant_unavalible_services),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}