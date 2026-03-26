package com.masdika.monja.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masdika.monja.data.model.Device
import com.masdika.monja.ui.theme.poppinsFont

@Composable
fun DashboardTopAppBarAction(
    devices: List<Device>,
    selectedDevice: Device?,
    onDeviceSelected: (Device) -> Unit
) {
    val isOnline = selectedDevice?.isOnline == true
    var expanded by remember { mutableStateOf(false) }

    Box(
        Modifier
            .size(15.dp)
            .clip(shape = CircleShape)
            .background(if (isOnline) Color(0xFF6CA651) else Color.Red)
    )
    Spacer(Modifier.width(5.dp))
    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(imageVector = Icons.Default.Person2, contentDescription = "Select Device")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        if (devices.isEmpty()) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "No devices available",
                        fontFamily = poppinsFont
                    )
                },
                onClick = { expanded = false }
            )
        } else {
            devices.forEach { device ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = device.macAddress,
                            fontWeight = if (device.macAddress == selectedDevice?.macAddress)
                                FontWeight.Bold else FontWeight.Normal,
                            fontFamily = poppinsFont,
                            color = if (device.isOnline) Color(0xFF6CA651) else MaterialTheme.colorScheme.onBackground
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