package com.example.rentify_proyecto_intermodular.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.home_owner.HomeOwnerScreen
import com.example.rentify_proyecto_intermodular.ui.home_tenant.HomeTenantScreen
import com.example.rentify_proyecto_intermodular.ui.incidents_owner.IncidentsOwnerScreen
import com.example.rentify_proyecto_intermodular.ui.incidents_tenant.IncidentsTenantScreen

/**
    This is the main composable function of the app.
    Once you log in, you are redirected here.
    Here is where the navigation bar is implemented and
    where you navigate to the different screens.
 */

@Composable
fun MainScreen(
    actualUser: User
){
    val navController = rememberNavController()
    val startDestination = Destinations.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destinations.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination.route
        ){
            composable(Destinations.HOME.route) {
                if (actualUser.ownedProperty != null){
                    HomeOwnerScreen(
                        modifier = Modifier.padding(contentPadding),
                        actualUser = actualUser
                    )
                }
                else if (actualUser.leasedProperty != null){
                    HomeTenantScreen(
                        actualUser = actualUser,
                        leasedProperty = actualUser.leasedProperty
                    )
                }
                else {
                    Text(text="ownedProperty y leasedProperty de actualUser son las dos null, y por tanto no se puede determinar si el usuario es propietario o inquilino (PREGUNTAR A GUILLE!)")
                }
            }
            composable(Destinations.INCIDENTS.route) {
                if (actualUser.ownedProperty != null) {
                    IncidentsOwnerScreen(
                        modifier = Modifier.padding(contentPadding),
                        actualUser = actualUser
                    )
                } else if (actualUser.leasedProperty != null) {
                    IncidentsTenantScreen(
                        modifier = Modifier.padding(contentPadding),
                        actualUser = actualUser
                    )
                }
            }
        }
    }
}
