package com.masdika.monja.ui.component.chart

import java.time.Instant

data class DataPoint(
    val value: Double,
    val timestamp: Instant,
    val labelX: String? = null
)