package com.masdika.monja.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masdika.monja.R
import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.ui.icon.StatusSpO2Icon
import com.masdika.monja.ui.icon.StatusTemperatureIcon
import com.masdika.monja.ui.theme.MatteLime
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.RedShimmer
import com.masdika.monja.ui.theme.YellowSea
import com.masdika.monja.ui.theme.openSansFont
import com.masdika.monja.ui.theme.poppinsFont
import com.masdika.monja.util.dateFormat
import java.util.Locale

@Composable
fun HistoryStatusCard(
    status: MedicalAlert,
    onIntentToGoogleMap: (latitude: String, longitude: String) -> Unit
) {
    val cardColor = when (status.newStatus) {
        "Severe" -> {
            RedShimmer
        }

        "Moderate" -> {
            YellowSea
        }

        else -> {
            MatteLime
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color.White
        ),
        onClick = {
            if (status.latitude != null && status.longitude != null) {
                onIntentToGoogleMap(status.latitude!!, status.longitude!!)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormat(status.createdAt),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFont
                )
                Text(
                    text = status.macAddress,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black.copy(0.8f),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFont
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), color = Color.Black)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(0.75f)) {
                    Text(
                        text = stringResource(R.string.label_history_card_status_changed),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = poppinsFont
                    )
                    Text(
                        text = "${status.oldStatus} ➔ ${status.newStatus}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        fontFamily = poppinsFont
                    )
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(0.25f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = StatusTemperatureIcon,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "%.1f",
                                status.temperatureAtTime
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = StatusSpO2Icon,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "${status.spo2AtTime} %",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            if (status.latitude != null && status.longitude != null) {
                Text(
                    text = stringResource(
                        R.string.format_location,
                        status.latitude!!,
                        status.longitude!!
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    fontFamily = openSansFont,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = stringResource(R.string.label_history_card_location_not_recorded),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryStatusCardPreview() {
    MonjaTheme {
        val medicalAlert = MedicalAlert(
            id = 0,
            macAddress = "01:01:01:01:01",
            oldStatus = "Moderate",
            newStatus = "Normal",
            temperatureAtTime = 34.2,
            spo2AtTime = 98,
            latitude = "0.000000",
            longitude = "0.000000",
            createdAt = "2026-03-25T10:00:00+7:00"
        )
        HistoryStatusCard(
            status = medicalAlert,
            onIntentToGoogleMap = { _, _ -> }
        )
    }
}