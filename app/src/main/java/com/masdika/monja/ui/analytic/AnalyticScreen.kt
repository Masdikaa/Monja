package com.masdika.monja.ui.analytic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.masdika.monja.ui.component.EmptyState
import com.masdika.monja.ui.component.MainTopAppBar
import com.masdika.monja.ui.component.chart.ChartConfig
import com.masdika.monja.ui.component.chart.DataPoint
import com.masdika.monja.ui.component.chart.LineChart
import com.masdika.monja.ui.theme.ElectricRed
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.NatureBlue
import com.masdika.monja.ui.theme.TuttiFruitti
import com.masdika.monja.ui.theme.poppinsFont
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun AnalyticScreen(
    viewModel: AnalyticViewModel,
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
        AnalyticChartContent(
            vitalState = vitalState,
            vitalType = viewModel.vitalType.lowercase(),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun AnalyticChartContent(
    vitalState: Result<List<Vitals>>,
    vitalType: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (vitalState) {
            is Result.Success -> {
                if (vitalState.data.isEmpty()) {
                    EmptyState(
                        icon = Icons.Outlined.Info,
                        title = "Empty Record",
                        message = "Currently there's no data available"
                    )
                } else {
                    val chartData = vitalState.data.map { vital ->
                        val value = when (vitalType) {
                            "temperature" -> vital.temperature
                            "heartrate" -> vital.heartrate.toDouble()
                            "spo2" -> vital.oxygenSaturation.toDouble()
                            else -> 0.0
                        }
                        DataPoint(value, Instant.parse(vital.createdAt))
                    }

                    val lineColor = when (vitalType) {
                        "temperature" -> ElectricRed
                        "heartrate" -> TuttiFruitti
                        "spo2" -> NatureBlue
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
                        showDots = false,
                        showXAxisLabels = true,
                        showTooltip = true,
                        showShadow = true,
                        pointColor = MaterialTheme.colorScheme.onBackground,
                        labelFontFamily = poppinsFont,
                        indicatorColor = MaterialTheme.colorScheme.onBackground,
                        tooltipBackgroundColor = MaterialTheme.colorScheme.onSurface,
                        tooltipTextColor = MaterialTheme.colorScheme.surface
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LineChart(
                            dataPoint = chartData,
                            config = chartConfig,
                            viewportDataPoints = 60,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(end = 16.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .weight(0.5f)
                        ) {
                            VitalStatistic(
                                vitalTypes = vitalType,
                                vitals = vitalState.data,
                            )
                        }
                    }
                }
            }

            is Result.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(42.dp))
            }

            is Result.Error -> {
                EmptyState(
                    icon = Icons.Default.WarningAmber,
                    title = "Internal Error",
                    message = "Couldn't connect to database\nPlease check your internet connection"
                )
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
//            vitalState = Result.Loading
//            vitalState = Result.Error(IllegalArgumentException("Error Test"))
        )

        val vitalType = "heartrate"
        val title = "Preview"

        Scaffold(
            topBar = {
                MainTopAppBar(title = title)
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AnalyticChartContent(
                vitalState = state.vitalState,
                vitalType = vitalType,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}