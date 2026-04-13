package com.masdika.monja.ui.dashboard.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.masdika.monja.ui.icon.GPSCenterIcon
import com.masdika.monja.ui.icon.HikerIcon

@Composable
fun MapControlButtons(
    isSatelliteMode: Boolean,
    isMapDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onToggleSatellite: () -> Unit,
    userLocation: Point?,
    devicePoint: Point?,
    isLocationLoading: Boolean,
    isOnline: Boolean,
    onCenterUser: () -> Unit,
    onCenterDevice: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier.padding(end = 5.dp, bottom = 5.dp)
    ) {
        // Color Theme Toggle
        IconButton(
            enabled = !isSatelliteMode,
            onClick = onToggleTheme,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.DarkGray.copy(0.6f),
                disabledContentColor = Color.LightGray.copy(0.6f)
            )
        ) {
            Icon(
                imageVector = if (isMapDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .padding(8.dp)
            )
        }

        // Satellite Toggle
        IconButton(
            onClick = onToggleSatellite,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isSatelliteMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                contentColor = if (isSatelliteMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(
                imageVector = Icons.Default.Layers,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .padding(8.dp)
            )
        }

        // Center to User Toggle
        IconButton(
            enabled = userLocation != null,
            onClick = onCenterUser,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.DarkGray.copy(0.6f),
                disabledContentColor = Color.LightGray.copy(0.8f)
            )
        ) {
            Icon(
                imageVector = GPSCenterIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .padding(8.dp)
            )
        }

        // Center to Device Toggle
        IconButton(
            enabled = !isLocationLoading && isOnline && devicePoint != null,
            onClick = onCenterDevice,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.DarkGray.copy(0.6f),
                disabledContentColor = Color.LightGray.copy(0.6f)
            )
        ) {
            Icon(
                imageVector = HikerIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .padding(8.dp)
            )
        }

    }
}