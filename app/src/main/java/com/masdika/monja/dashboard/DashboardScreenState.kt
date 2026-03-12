package com.masdika.monja.dashboard

import com.masdika.monja.data.model.Device

data class DashboardScreenState(
    val devices: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val dataLoading: Boolean = false,
)