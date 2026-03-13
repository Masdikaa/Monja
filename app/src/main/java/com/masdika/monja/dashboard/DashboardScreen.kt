package com.masdika.monja.dashboard

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masdika.monja.component.MainTopAppBar
import com.masdika.monja.dashboard.component.TopAppBarAction
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.ui.theme.MonjaTheme

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = "Dashboard"
            ) {
                TopAppBarAction(
                    devices = state.devices,
                    selectedDevice = state.selectedDevice,
                    onDeviceSelected = { device -> viewModel.selectDevice(device) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        DashboardContent(
            devices = state.devices,
            isDataLoading = state.dataLoading,
            vitals = state.vitals,
            location = state.location,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun DashboardContent(
    devices: List<Device>,
    vitals: Vitals?,
    location: Location?,
    isDataLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (isDataLoading) {
            CircularProgressIndicator(Modifier.size(40.dp))
        } else if (devices.isEmpty()) {
            Text("Empty Devices")
        } else {
            devices.forEach { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Controller MAC:", style = MaterialTheme.typography.labelMedium)
                        Text(text = device.macAddress, style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (device.isOnline) "🟢 - ONLINE" else "🔴 - OFFLINE",
                            color = if (device.isOnline) Color(0xFF4CAF50) else Color.Red,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Last seen: ${device.lastSeen}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Temperature\t\t= ${vitals?.temperature ?: "--"}")
                Text(text = "Hear Rate\t\t\t\t\t= ${vitals?.heartrate ?: "--"}")
                Text(text = "SpO2\t\t\t\t\t\t\t\t\t= ${vitals?.oxygenSaturation ?: "--"}")
                Spacer(Modifier.height(10.dp))
                Text(text = "Latitude\t\t\t\t\t\t= ${location?.latitude ?: "Searching for latitude"}")
                Text(text = "Longitude\t\t\t\t\t= ${location?.longitude ?: "Searching for longitude"}")
            }
        }
    }
}

@Preview(device = PIXEL_9, showSystemUi = true)
@Preview(device = PIXEL_9, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DashboardContentPreview() {
    MonjaTheme {
        val devices = listOf(
            Device(
                macAddress = "AH:JK:O7:OH:X4",
                isOnline = true,
                lastSeen = "LastSeen",
                createdAt = "2026-03-13 18:30:08.171222+00"
            ),
            Device(
                macAddress = "8H:81:O7:OH:T4",
                isOnline = false,
                lastSeen = "LastSeen",
                createdAt = "2026-03-13 19:30:08.171222+00"
            )
        )
        val vitals = Vitals(
            temperature = 33.9,
            heartrate = 101,
            oxygenSaturation = 90
        )
        val location = Location(
            latitude = "-7.610600",
            longitude = "111.443398"
        )
        Scaffold(
            topBar = {
                MainTopAppBar(
                    title = "Dashboard"
                ) {
                    TopAppBarAction(
                        devices = devices,
                        selectedDevice = devices[0],
                        onDeviceSelected = { }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            DashboardContent(
                devices = devices,
                isDataLoading = false,
                vitals = vitals,
                location = location,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}