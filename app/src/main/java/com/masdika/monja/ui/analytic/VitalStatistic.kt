package com.masdika.monja.ui.analytic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.poppinsFont
import com.masdika.monja.util.dateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun VitalStatistic(
    vitalTypes: String,
    vitals: List<Vitals>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        when (vitalTypes) {
            "temperature" -> {

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

                val totalReadings = vitals.size

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text(
                            text = "Average",
                            fontSize = 24.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "$totalReadings",
                            fontSize = 24.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "%.1f", averageHr),
                            fontSize = 65.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Peak",
                                fontSize = 18.sp,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "$peakHr",
                                fontSize = 32.sp,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = dateFormat(peakHeartRateRecord?.createdAt.toString()),
                                fontSize = 12.sp,
                                fontFamily = poppinsFont,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 5.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Rest",
                                fontSize = 18.sp,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "$restingHr",
                                fontSize = 32.sp,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = dateFormat(restingHeartRateRecord?.createdAt.toString()),
                                fontSize = 12.sp,
                                fontFamily = poppinsFont,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                }
            }

            "spo2" -> {

            }

            else -> {}
        }
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