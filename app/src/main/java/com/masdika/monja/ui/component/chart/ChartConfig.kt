package com.masdika.monja.ui.component.chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

data class ChartConfig(
    val yAxisLabel: String = "",
    val yAxisMin: Double? = null,
    val yAxisMax: Double? = null,
    val showIndicators: Boolean = true,
    val showDots: Boolean = true,
    val showXAxisLabels: Boolean = true,
    val xAxisLabelSpacingDp: Float = 60f,
    val labelFontFamily: FontFamily = FontFamily.Default,
    val lineColor: Color = Color.Red,
    val pointColor: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val indicatorColor: Color = Color.Gray,
    val tooltipBackgroundColor: Color = Color.DarkGray,
    val tooltipTextColor: Color = Color.White
)