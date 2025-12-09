package com.example.rentify_proyecto_intermodular.ui.home_tenant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.model.User

@Composable
fun HomeTenantScreen(modifier: Modifier = Modifier, user: User, property: Property) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.scrim
        )

        Text(
            text = stringResource(R.string.home_tenant_slogan, user.firstName),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )


    }
}
