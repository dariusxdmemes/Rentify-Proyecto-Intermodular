package com.example.rentify_proyecto_intermodular.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.model.User

/*
CREATE, RETRIEVE, UPDATE, DELETE

SETTINGS TO IMPLEMENT:

[X] UPDATE INFORMATION
[ ] LOG OUT
[ ] DELETE ACCOUNT
 */

@Composable
fun TenantSettingsScreen(
    modifier: Modifier = Modifier,
    actualUser: User,
    onUserLogout: () -> Unit,
    onUserUpdate: (User)->Unit
){
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.scrim
        )
        Text(
            text = stringResource(R.string.settings_slogan),
            style = MaterialTheme.typography.titleMedium
        )

        Column (
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UpdateAccountInfoCard(actualUser, context, onUserUpdate)
            LogoutCard(onUserLogout)
            DeleteAccountCard(actualUser, onUserLogout, context)
        }
    }
}
