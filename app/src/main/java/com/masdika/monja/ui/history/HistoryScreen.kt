package com.masdika.monja.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.data.utils.Result
import com.masdika.monja.ui.component.MainBottomBar
import com.masdika.monja.ui.component.MainTopAppBar
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.poppinsFont
import com.masdika.monja.util.openGoogleMaps

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is HistoryScreenEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = "History",
            ) {
                HistoryTopAppBar(
                    onDeleteClick = { viewModel.showDeleteConfirmation() }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) { message ->
                Snackbar(
                    snackbarData = message,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (state.showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirm = viewModel::deleteAllMedicalAlerts,
                onDismiss = viewModel::dismissDeleteConfirmation
            )
        }
        HistoryContent(
            statusState = state.statusState,
            macAddress = state.macAddress,
            onOpenGoogleMaps = { latitude, longitude ->
                openGoogleMaps(
                    context,
                    latitude,
                    longitude
                )
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HistoryContent(
    macAddress: String,
    statusState: Result<List<MedicalAlert>>,
    onOpenGoogleMaps: (latitude: String, longitude: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (statusState) {
            is Result.Loading -> {
                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }

            is Result.Success -> {
                Spacer(Modifier.height(4.dp))
                when {
                    macAddress.isEmpty() -> {
                        HistoryEmptyState(
                            icon = Icons.Outlined.Info,
                            title = "Device MAC Address not Found",
                            message = "Please select a device to show history"
                        )
                    }

                    statusState.data.isEmpty() -> {
                        HistoryEmptyState(
                            icon = Icons.Outlined.MonitorHeart,
                            title = "Empty Histories",
                            message = "No medical alerts found in this device",
                        )
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(statusState.data) { status ->
                                HistoryStatusCard(
                                    status = status,
                                    onIntentToGoogleMap = onOpenGoogleMaps
                                )
                            }
                        }
                    }
                }
            }

            is Result.Error -> {
                HistoryEmptyState(
                    icon = Icons.Default.WarningAmber,
                    title = "Internal Error",
                    message = statusState.message ?: "Unknown error"
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete all History ?",
                fontFamily = poppinsFont,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "This action can't be undone. All history status for this device will be permanently deleted.",
                fontFamily = poppinsFont,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Delete",
                    fontFamily = poppinsFont
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = poppinsFont
                )
            }
        }
    )
}

@Preview(device = PIXEL_9, showBackground = true)
@Composable
private fun HistoryContentPreview() {
    MonjaTheme {
        val navController = rememberNavController()
        val medicalAlerts = listOf(
            MedicalAlert(
                id = 0,
                macAddress = "01:01:01",
                oldStatus = "Severe",
                newStatus = "Normal",
                temperatureAtTime = 34.2,
                spo2AtTime = 98,
                latitude = "0.000000",
                longitude = "0.000000",
                createdAt = "2026-03-25T10:00:00+7:00"
            ),
            MedicalAlert(
                id = 0,
                macAddress = "01:01:01",
                oldStatus = "Normal",
                newStatus = "Moderate",
                temperatureAtTime = 34.2,
                spo2AtTime = 98,
                latitude = "0.000000",
                longitude = "0.000000",
                createdAt = "2026-03-25T10:10:00+7:00"
            ),
            MedicalAlert(
                id = 0,
                macAddress = "01:01:01",
                oldStatus = "Moderate",
                newStatus = "Severe",
                temperatureAtTime = 34.2,
                spo2AtTime = 98,
                latitude = "0.000000",
                longitude = "0.000000",
                createdAt = "2026-03-25T10:20:00+7:00"
            )
        )
        Scaffold(
            topBar = {
                MainTopAppBar(
                    title = "Dashboard"
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            },
            bottomBar = {
                MainBottomBar(navController = navController)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            HistoryContent(
                macAddress = medicalAlerts[0].macAddress,
//              statusState = Result.Loading,
                statusState = Result.Success(medicalAlerts),
//              statusState = Result.Error(Exception("Example Error"), message = "Error Happened"),
                onOpenGoogleMaps = { _, _ -> },
                modifier = Modifier.padding(it)
            )
        }
    }
}