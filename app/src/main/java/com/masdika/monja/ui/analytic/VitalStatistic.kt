package com.masdika.monja.ui.analytic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.poppinsFont
import com.masdika.monja.util.dateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun VitalStatistic(
    vitalTypes: String,
    vitals: List<Vitals>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        when (vitalTypes) {
            "temperature" -> {
                val minTemperatureRecord: Vitals? = vitals.minByOrNull { it.temperature }
                val maxTemperatureRecord: Vitals? = vitals.maxByOrNull { it.temperature }
                val minTemperature = minTemperatureRecord?.temperature
                val maxTemperature = maxTemperatureRecord?.temperature
                val averageTemperature = if (vitals.isNotEmpty()) {
                    vitals.map { it.temperature }.average()
                } else {
                    0.0
                }
                val totalData = vitals.size

                VitalStatisticContent(
                    average = "%.2f".format(averageTemperature),
                    max = maxTemperature.toString(),
                    min = minTemperature.toString(),
                    totalData = totalData,
                    maxAt = dateFormat(maxTemperatureRecord?.createdAt.toString()),
                    minAt = dateFormat(minTemperatureRecord?.createdAt.toString()),
                    primaryText = "Average Temperature",
                    secondaryTextLeft = "Max",
                    secondaryTextRight = "Min"
                )
            }

            "heartrate" -> {
                val restingHeartRateRecord: Vitals? = vitals.minByOrNull { it.heartrate }
                val peakHeartRateRecord: Vitals? = vitals.maxByOrNull { it.heartrate }
                val restingHr = restingHeartRateRecord?.heartrate
                val peakHr = peakHeartRateRecord?.heartrate
                val averageHr = if (vitals.isNotEmpty()) {
                    vitals.map { it.heartrate }.average()
                } else {
                    0.0
                }
                val totalData = vitals.size

                VitalStatisticContent(
                    average = "%.2f".format(averageHr),
                    max = peakHr.toString(),
                    min = restingHr.toString(),
                    totalData = totalData,
                    maxAt = dateFormat(peakHeartRateRecord?.createdAt.toString()),
                    minAt = dateFormat(restingHeartRateRecord?.createdAt.toString()),
                    primaryText = "Average Heart Rate",
                    secondaryTextLeft = "Peak",
                    secondaryTextRight = "Rest"
                )
            }

            "spo2" -> {
                val minSpO2Record: Vitals? = vitals.minByOrNull { it.oxygenSaturation }
                val maxSpO2Record: Vitals? = vitals.maxByOrNull { it.oxygenSaturation }
                val minSpO2 = minSpO2Record?.oxygenSaturation
                val maxSpO2 = maxSpO2Record?.oxygenSaturation
                val averageSpO2 = if (vitals.isNotEmpty()) {
                    vitals.map { it.oxygenSaturation }.average()
                } else {
                    0.0
                }
                val totalData = vitals.size

                VitalStatisticContent(
                    average = "%.2f".format(averageSpO2),
                    max = maxSpO2.toString(),
                    min = minSpO2.toString(),
                    totalData = totalData,
                    maxAt = dateFormat(maxSpO2Record?.createdAt.toString()),
                    minAt = dateFormat(minSpO2Record?.createdAt.toString()),
                    primaryText = "Average SpO2",
                    secondaryTextLeft = "Max",
                    secondaryTextRight = "Min"
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun VitalStatisticContent(
    average: String,
    max: String,
    min: String,
    totalData: Int,
    maxAt: String,
    minAt: String,
    primaryText: String,
    secondaryTextLeft: String,
    secondaryTextRight: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = primaryText,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = average,
            style = MaterialTheme.typography.displayLarge,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(
            thickness = 1.5.dp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = secondaryTextLeft,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = max,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = maxAt,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = poppinsFont,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = secondaryTextRight,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = min,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = minAt,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = poppinsFont,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = buildAnnotatedString {
                append("Statistics of ")
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) { append("$totalData") }
                append(" data entries recorded")
            },
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = poppinsFont,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun VitalStatisticPreview() {
    MonjaTheme {
        val vitals = List(50) { index ->
            Vitals(
                temperature = 28 + (Math.random() * 8),
                heartrate = 60 + (Math.random() * 60).toInt(),
                oxygenSaturation = 95 + (Math.random() * 5).toInt(),
                createdAt = Instant.now().minus(index.toLong(), ChronoUnit.MINUTES).toString()
            )
        }.reversed()

        VitalStatistic(
            vitalTypes = "heartrate",
            vitals = vitals
        )
    }
}