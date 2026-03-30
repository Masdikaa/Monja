package com.masdika.monja.ui.analytic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.utils.Result
import com.masdika.monja.ui.component.MainTopAppBar
import com.masdika.monja.ui.component.chart.ChartConfig
import com.masdika.monja.ui.component.chart.DataPoint
import com.masdika.monja.ui.component.chart.LineChart
import com.masdika.monja.ui.dashboard.bottomsheet.VitalColors
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.poppinsFont
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun AnalyticScreen(
    viewModel: AnalyticViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val vitalState = state.vitalState

    val title = when (viewModel.vitalType.lowercase()) {
        "temperature" -> "Body Temperature"
        "heartrate" -> "Heart Rate"
        "spo2" -> "Oxygen Saturation (SpO2)"
        else -> "Analytics"
    }

    Scaffold(
        topBar = {
            MainTopAppBar(title = title)
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (vitalState) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }

                is Result.Success -> {
                    val vitals = vitalState.data

                    if (vitals.isEmpty()) {
                        Text("No data available!")
                    } else {
                        AnalyticChartContent(
                            vitals = vitals,
                            vitalType = viewModel.vitalType.lowercase(),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                is Result.Error -> {
                    Text("Error")
                }
            }
        }
    }
}

@Composable
private fun AnalyticChartContent(
    vitals: List<Vitals>,
    vitalType: String,
    modifier: Modifier = Modifier
) {
    val chartData = vitals.map { vital ->
        val value = when (vitalType) {
            "temperature" -> vital.temperature
            "heartrate" -> vital.heartrate.toDouble()
            "spo2" -> vital.oxygenSaturation.toDouble()
            else -> 0.0
        }
        DataPoint(value, Instant.parse(vital.createdAt))
    }

    val lineColor = when (vitalType) {
        "temperature" -> VitalColors.TemperatureGradient.last().second
        "heartrate" -> VitalColors.HeartrateGradient.last().second
        "spo2" -> VitalColors.OxygenSaturationGradient.last().second
        else -> Color.Gray
    }

    val chartMinValue = when (vitalType) {
        "temperature" -> 20.0
        "heartrate" -> 40.0
        "spo2" -> 60.0
        else -> 0.0
    }

    val chartMaxValue = when (vitalType) {
        "temperature" -> 50.0
        "heartrate" -> 220.0
        "spo2" -> 100.0
        else -> 0.0
    }

    val chartConfig = ChartConfig(
        lineColor = lineColor,
        yAxisMin = chartMinValue,
        yAxisMax = chartMaxValue,
        showIndicators = true,
        showDots = true,
        showXAxisLabels = true,
        showTooltip = true,
        pointColor = MaterialTheme.colorScheme.onBackground,
        labelFontFamily = poppinsFont,
        indicatorColor = MaterialTheme.colorScheme.onBackground,
        tooltipBackgroundColor = MaterialTheme.colorScheme.onSurface,
        tooltipTextColor = MaterialTheme.colorScheme.surface
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        LineChart(
            dataPoint = chartData,
            config = chartConfig,
            viewportDataPoints = 60,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .weight(0.4f)
        ) {
            when (vitalType) {
                "temperature" -> {}

                "heartrate" -> {
                    VitalStatistic(
                        vitalTypes = vitalType,
                        vitals = vitals,
                    )
                }

                "spo2" -> {}

                else -> {}
            }
        }
    }
}

@Preview
@Composable
private fun AnalyticScreenPreview() {
    MonjaTheme {
        val now = Instant.now()
        val vitals = List(400) { index ->
            Vitals(
                temperature = 28 + (Math.random() * 8),
                heartrate = 60 + (Math.random() * 60).toInt(),
                oxygenSaturation = 95 + (Math.random() * 5).toInt(),
                createdAt = now.minus(index.toLong(), ChronoUnit.MINUTES).toString()
            )
        }.reversed()

        val state = AnalyticScreenState(
            vitalState = Result.Success(vitals)
        )

        val vitalType = "heartrate"
        val title = "Preview"

        Scaffold(
            topBar = {
                MainTopAppBar(title = title)
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnalyticChartContent(
                    vitals = vitals,
                    vitalType = vitalType,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}