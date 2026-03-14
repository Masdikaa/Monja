package com.masdika.monja.ui.dashboard

import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.Vitals

data class DashboardScreenState(
    val devices: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val dataLoading: Boolean = false,
    val vitals: Vitals? = null,
    val location: Location? = null
)