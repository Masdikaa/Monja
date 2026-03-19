package com.masdika.monja.ui.dashboard

import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.Vitals

data class DashboardScreenState(
    val devices: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val vitals: Vitals? = null,
    val location: Location? = null,
    val healthStatus: HealthStatus? = null,

    // Loading
    val deviceLoading: Boolean = true,
    val vitalsLoading: Boolean = false,
    val locationLoading: Boolean = false,
    val healthStatusLoading: Boolean = false
)