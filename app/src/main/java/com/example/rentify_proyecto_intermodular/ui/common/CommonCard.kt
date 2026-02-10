package com.example.rentify_proyecto_intermodular.ui.common

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentify_proyecto_intermodular.ui.theme.RentifyProyectoIntermodularTheme

@Composable
fun CommonCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector?,
    expanded: Boolean,
    content: @Composable ()->Unit
){
    var expandedState by remember { mutableStateOf(expanded) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column {

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (icon != null) {
                    Icon(
                        modifier = Modifier.padding(5.dp),
                        imageVector = icon,
                        contentDescription = null
                    )
                }

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    modifier = Modifier.padding(5.dp),
                    onClick = { expandedState = !expandedState }
                ) {
                    Icon(
                        imageVector = if (expandedState)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(visible = expandedState) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RetractedCommonCardPreview() {
    RentifyProyectoIntermodularTheme() {
        CommonCard(
            title="Test",
            icon = Icons.Default.Home,
            expanded = false
        ) {
            Column {
                Text(
                    text = "First line"
                )
                Text(
                    text = "Second line"
                )
                Text(
                    text = "Third line"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedCommonCardPreview() {
    RentifyProyectoIntermodularTheme() {
        CommonCard(
            title="Test",
            icon = Icons.Default.Home,
            expanded = true
        ) {
            Column {
                Text(
                    text = "First line"
                )
                Text(
                    text = "Second line"
                )
                Text(
                    text = "Third line"
                )
            }
        }
    }
}

