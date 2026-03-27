package com.masdika.monja.ui.dashboard.bottomsheet

import androidx.compose.ui.graphics.Color

object VitalColors {
    val TemperatureGradient = arrayOf(
        0.0f to Color(0xFF9C0101),
        0.6f to Color(0xFFC00000),
        1f to Color(0xFFE30202)
    )
    val HeartrateGradient = arrayOf(
        0.0f to Color(0xFF740021),
        0.6f to Color(0xFF8D0033),
        1f to Color(0xFFBD3246)
    )
    val OxygenSaturationGradient = arrayOf(
        0.0f to Color(0xFF0F2854),
        0.6f to Color(0xFF1C4D8D),
        1f to Color(0xFF4988C4)
    )
    val StatusGradient = arrayOf(
        0.0f to Color(0xFF7A1CAC),
        0.6f to Color(0xFFAD49E1),
        1f to Color(0xFFC8A1E0)
    )
}