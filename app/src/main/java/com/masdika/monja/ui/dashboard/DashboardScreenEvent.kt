package com.masdika.monja.ui.dashboard

import com.masdika.monja.util.UiText

sealed interface DashboardScreenEvent {
    data class ShowEmptyDevicesSnackbar(val message: UiText) : DashboardScreenEvent
    data class ShowDeviceConnectionSnackbar(val message: UiText) : DashboardScreenEvent
    data class ShowEvacuationAlert(val macAddress: String) : DashboardScreenEvent
}