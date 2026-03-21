package com.masdika.monja.ui.dashboard

import com.masdika.monja.data.model.Device
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.model.Location
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.utils.Result

data class DashboardScreenState(
    val deviceState: Result<List<Device>> = Result.Loading,
    val selectedDevice: Device? = null,

    // Vitals
    val vitalsState: Result<Vitals?> = Result.Loading,
    val locationState: Result<Location?> = Result.Loading,
    val healthStatusState: Result<HealthStatus?> = Result.Loading,
)