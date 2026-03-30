package com.masdika.monja.ui.component.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masdika.monja.ui.theme.MonjaTheme
import java.time.Instant
import java.time.temporal.ChronoUnit

@Preview
@Composable
private fun LineChartPreview() {
    val baseTime = Instant.now()

    val dummyData = listOf(
        DataPoint(80.0, baseTime),
        DataPoint(81.0, baseTime.plus(1, ChronoUnit.MINUTES)),
        DataPoint(80.0, baseTime.plus(2, ChronoUnit.MINUTES)),
        DataPoint(85.0, baseTime.plus(3, ChronoUnit.MINUTES)),
        DataPoint(87.0, baseTime.plus(5, ChronoUnit.MINUTES)),
        DataPoint(89.0, baseTime.plus(9, ChronoUnit.MINUTES)),
        DataPoint(93.0, baseTime.plus(11, ChronoUnit.MINUTES)),
        DataPoint(90.0, baseTime.plus(12, ChronoUnit.MINUTES)),
        DataPoint(93.0, baseTime.plus(13, ChronoUnit.MINUTES)),
        DataPoint(101.0, baseTime.plus(15, ChronoUnit.MINUTES)),
        DataPoint(110.0, baseTime.plus(18, ChronoUnit.MINUTES)),
        DataPoint(118.0, baseTime.plus(19, ChronoUnit.MINUTES)),
        DataPoint(112.0, baseTime.plus(21, ChronoUnit.MINUTES)),
        DataPoint(117.0, baseTime.plus(22, ChronoUnit.MINUTES)),
        DataPoint(123.0, baseTime.plus(23, ChronoUnit.MINUTES)),
        DataPoint(128.0, baseTime.plus(27, ChronoUnit.MINUTES)),
        DataPoint(132.0, baseTime.plus(30, ChronoUnit.MINUTES)),
    )

    val config = ChartConfig(
        lineColor = Color(0xFF00E676),
        showXAxisLabels = true,
        showIndicators = false,
        backgroundColor = MaterialTheme.colorScheme.background,
        pointColor = Color.Transparent,
        indicatorColor = MaterialTheme.colorScheme.onBackground,
        tooltipBackgroundColor = Color.DarkGray,
        tooltipTextColor = Color.White
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        LineChart(
            dataPoint = dummyData,
            config = config,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(showSystemUi = true, device = PIXEL_9)
@Composable
private fun ScrollableLineChartPreview() {
    val baseTime = Instant.now()

    val dummyData = List(30) { index ->
        val time = baseTime.minus((180 - (index * 5)).toLong(), ChronoUnit.MINUTES)
        val value = 70.0 + (Math.random() * 70)
        DataPoint(value, time)
    }

    val config = ChartConfig(
        lineColor = Color.Red,
        showXAxisLabels = true,
        yAxisLabel = "Preview",
        showIndicators = true,
        showShadow = true,
        backgroundColor = MaterialTheme.colorScheme.background,
        indicatorColor = Color.Black,
        pointColor = MaterialTheme.colorScheme.onBackground,
        tooltipBackgroundColor = Color.DarkGray,
        tooltipTextColor = Color.White
    )

    MonjaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 100.dp)
        ) {
            Text(
                text = "Scrollable Chart (Viewport: 1 Hour, Total data: 3 hours",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            LineChart(
                dataPoint = dummyData,
                config = config,
                viewportDataPoints = 60,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            )
        }
    }
}