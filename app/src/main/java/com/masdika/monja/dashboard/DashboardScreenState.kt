package com.masdika.monja.dashboard

import com.masdika.monja.data.model.Device

data class DashboardScreenState(
    val devices: List<Device> = emptyList(),
    val dataLoading: Boolean = false
)