package com.masdika.monja.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masdika.monja.data.model.Device

@Composable
fun TopAppBar(
    devices: List<Device>,
    selectedDevice: Device?,
    onDeviceSelected: (Device) -> Unit
) {
    val isOnline = selectedDevice?.isOnline == true
    var expanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .statusBarsPadding()
            .height(64.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Map",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Box(
                Modifier
                    .size(15.dp)
                    .clip(shape = CircleShape)
                    .background(if (isOnline) Color(0xFF006D2C) else Color.Red)
            )

            Spacer(Modifier.width(5.dp))

            Box(
                modifier = Modifier.clickable(onClick = { expanded = true })
            ) {
                Icon(
                    imageVector = Icons.Default.Person2,
                    contentDescription = "Select Device"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (devices.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No devices available") },
                        onClick = { expanded = false }
                    )
                } else {
                    devices.forEach { device ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = device.macAddress,
                                    fontWeight = if (device.macAddress == selectedDevice?.macAddress)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            },
                            onClick = {
                                onDeviceSelected(device)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}