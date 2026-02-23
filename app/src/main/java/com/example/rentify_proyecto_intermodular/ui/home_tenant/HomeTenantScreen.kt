package com.example.rentify_proyecto_intermodular.ui.home_tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rentify_proyecto_intermodular.R
import com.example.rentify_proyecto_intermodular.data.api.getOwnerUser
import com.example.rentify_proyecto_intermodular.data.api.getServicesByProperty
import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.ui.common.CommonCard
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTenantScreen(
    modifier: Modifier = Modifier,
    actualUser: User,
    leasedProperty: Property?
) {
    var services by remember { mutableStateOf<Service?>(null) }
    var owner by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var fotosPropiedades by remember { mutableStateOf(listOf<String>()) }
    var fotosPropiedadesTest = listOf<String>(
        "https://i.pinimg.com/736x/b2/01/72/b20172b4535a922b32c5effdc19c1173.jpg",
        "https://i.pinimg.com/736x/cf/f7/45/cff7451f24118f6efa98620c60700530.jpg",
        "https://i.pinimg.com/736x/b0/36/d8/b036d8f10516eb1845de912855213a34.jpg",
        "https://i.pinimg.com/736x/b8/c5/64/b8c56428d3ab4bb90f2086b3b0fa5ecf.jpg"
    )

    if (leasedProperty != null) {
        LaunchedEffect(leasedProperty) {
            isLoading = true
            try {
                coroutineScope {
                    val servicesDeferred = async {
                        getServicesByProperty(leasedProperty.id)
                    }
                    val ownerDeferred = async {
                        getOwnerUser(leasedProperty.owner_fk)
                    }

                    services = servicesDeferred.await()
                    owner = ownerDeferred.await()
                }
            } finally {
                isLoading = false
            }
        }
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
                .padding(12.dp)
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
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 40.dp)
            )

            if (leasedProperty != null) {
                CommonCard(
                    title = leasedProperty.address,
                    icon = Icons.Default.Home,
                    expanded = false,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isLoading) {
                            Text(text = stringResource(R.string.wait))
                        } else {
                            Text(
                                text = "${stringResource(R.string.home_tenant_name_placeholder)} " +
                                        "${owner?.firstName ?: stringResource(R.string.home_tenant_unavalible_owner)} " +
                                        "${owner?.lastName ?: stringResource(R.string.home_tenant_unavalible_owner)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${stringResource(R.string.home_tenant_price)} ${leasedProperty.alquiler} ${
                                    stringResource(
                                        R.string.euro
                                    )
                                }",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${stringResource(R.string.home_tenant_services)} ${services?.included}"
                                    ?: stringResource(R.string.home_tenant_unavalible_services),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }
                }

                HorizontalUncontainedCarousel(
                    state = rememberCarouselState { fotosPropiedadesTest.count() },
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 45.dp, bottom = 10.dp),
                    itemSpacing = 10.dp,
                    itemWidth = 160.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { i ->
                    val items = fotosPropiedadesTest[i]

                    AsyncImage(
                        model = items,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "${actualUser.leasedProperty?.address}",
                    modifier = Modifier.basicMarquee(),
                    fontSize = 15.sp
                )
            }
            else {
                Text(stringResource(R.string.home_tenant_no_property))
            }
        }
    }
}