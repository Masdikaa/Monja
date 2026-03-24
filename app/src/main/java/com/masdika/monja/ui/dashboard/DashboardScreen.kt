package com.masdika.monja.ui.dashboard

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.utils.Result
import com.masdika.monja.ui.component.MainBottomBar
import com.masdika.monja.ui.component.MainTopAppBar
import com.masdika.monja.ui.dashboard.bottomsheet.DashboardBottomSheet
import com.masdika.monja.ui.dashboard.map.DashboardMap
import com.masdika.monja.ui.icon.ArrowUpIcon
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.util.RequestLocationPermission

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val devices = (state.deviceState as? Result.Success)?.data ?: emptyList()
    val deviceLoading = state.deviceState is Result.Loading

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is DashboardScreenEvent.ShowEmptyDevicesSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Long
                    )
                }

                is DashboardScreenEvent.ShowDeviceConnectionSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = "${event.macAddress} is ${if (event.isOnline) "Online" else "Offline"}",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = "Dashboard"
            ) {
                TopAppBarAction(
                    devices = devices,
                    selectedDevice = state.selectedDevice,
                    onDeviceSelected = { device -> viewModel.selectDevice(device) }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopCenter)
                    .padding(top = 120.dp)
            ) { message ->
                Snackbar(
                    snackbarData = message,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        DashboardContent(
            selectedDevice = state.selectedDevice,
            vitalsState = state.vitalsState,
            locationState = state.locationState,
            healthStatusState = state.healthStatusState,
            deviceLoading = deviceLoading,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    selectedDevice: Device?,
    vitalsState: Result<Vitals?>,
    locationState: Result<Location?>,
    healthStatusState: Result<HealthStatus?>,
    deviceLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier = modifier.fillMaxSize()) {
        if (deviceLoading) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            RequestLocationPermission(
                onPermissionGranted = {
                    DashboardMap(
                        macAddress = selectedDevice?.macAddress ?: "",
                        isOnline = selectedDevice?.isOnline ?: false,
                        locationState = locationState,
                        isGpsEnabled = true,
                    )
                },
                onPermissionDenied = {
                    DashboardMap(
                        macAddress = selectedDevice?.macAddress ?: "",
                        isOnline = selectedDevice?.isOnline ?: false,
                        locationState = locationState,
                        isGpsEnabled = false,
                    )
                }
            )
            if (selectedDevice != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(width = 120.dp, height = 25.dp)
                        .background(
                            shape = RoundedCornerShape(
                                topStart = CornerSize(16.dp),
                                topEnd = CornerSize(16.dp),
                                bottomEnd = CornerSize(0),
                                bottomStart = CornerSize(0)
                            ),
                            color = MaterialTheme.colorScheme.background
                        )
                        .clickable(onClick = { showBottomSheet = true })
                ) {
                    Icon(
                        imageVector = ArrowUpIcon,
                        contentDescription = "Arrow Up Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (showBottomSheet) {
                    DashboardBottomSheet(
                        sheetState = sheetState,
                        vitalsState = vitalsState,
                        healthStatusState = healthStatusState,
                        isOnline = selectedDevice.isOnline,
                        onDismissSheetState = { showBottomSheet = false }
                    )
                }
            }
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
        val status = HealthStatus("Normal")
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
                vitalsState = Result.Success(vitals),
                locationState = Result.Success(location),
                healthStatusState = Result.Success(status),
                deviceLoading = false,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}