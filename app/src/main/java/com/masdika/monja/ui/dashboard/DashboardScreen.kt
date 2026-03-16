package com.masdika.monja.ui.dashboard

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.ui.component.MainBottomBar
import com.masdika.monja.ui.component.MainTopAppBar
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.util.RequestLocationPermission

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
            selectedDevice = state.selectedDevice,
            deviceLoading = state.deviceLoading,
            locationLoading = state.locationLoading,
            vitalsLoading = state.vitalsLoading,
            vitals = state.vitals,
            location = state.location,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun DashboardContent(
    selectedDevice: Device?,
    vitals: Vitals?,
    location: Location?,
    deviceLoading: Boolean,
    locationLoading: Boolean,
    vitalsLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (deviceLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (selectedDevice == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tidak ada perangkat yang tersedia.")
            }
        } else {
            // 2. Device is ready, render Map and Vitals
            RequestLocationPermission(
                onPermissionGranted = {
                    DashboardMap(
                        macAddress = selectedDevice.macAddress,
                        isOnline = selectedDevice.isOnline,
                        deviceLocation = location,
                        isGpsEnabled = true,
                        isLocationLoading = locationLoading,
                        modifier = Modifier.fillMaxSize()
                    )
                },
                onPermissionDenied = {
                    DashboardMap(
                        macAddress = selectedDevice.macAddress,
                        isOnline = selectedDevice.isOnline,
                        deviceLocation = location,
                        isGpsEnabled = false,
                        isLocationLoading = locationLoading, // Oper to specific parameter
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
        }
    }
}

@Preview(device = PIXEL_9, showSystemUi = true)
@Preview(device = PIXEL_9, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DashboardContentPreview() {
    MonjaTheme {
        val navController = rememberNavController()

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
            bottomBar = {
                MainBottomBar(navController = navController)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            DashboardContent(
                selectedDevice = devices[0],
                deviceLoading = false,
                locationLoading = true,
                vitalsLoading = false,
                vitals = vitals,
                location = location,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}