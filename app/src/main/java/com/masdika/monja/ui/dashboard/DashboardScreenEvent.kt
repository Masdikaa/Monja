package com.masdika.monja.ui.dashboard

sealed interface DashboardScreenEvent {
    data class ShowEmptyDevicesSnackbar(val message: String) : DashboardScreenEvent
    data class ShowDeviceConnectionSnackbar(
        val macAddress: String,
        val isOnline: Boolean
    ) : DashboardScreenEvent
}