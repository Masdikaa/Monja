package com.masdika.monja.ui.dashboard.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masdika.monja.data.model.HealthStatus
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.utils.Result
import com.masdika.monja.ui.icon.BodyTemperatureIcon
import com.masdika.monja.ui.icon.HealthStatusIcon
import com.masdika.monja.ui.icon.HeartrateIcon
import com.masdika.monja.ui.icon.SpO2Icon
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.poppinsFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBottomSheet(
    sheetState: SheetState,
    vitalsState: Result<Vitals?>,
    healthStatusState: Result<HealthStatus?>,
    isOnline: Boolean,
    onDismissSheetState: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissSheetState() },
        sheetState = sheetState
    ) {
        when (healthStatusState) {
            is Result.Loading -> {
                VitalCard(
                    title = "Status",
                    value = null,
                    imageIcon = HealthStatusIcon,
                    colorStops = VitalColors.PurpleGradient,
                    isLoading = true,
                    isOnline = isOnline,
                    onClick = {},
                    valueTextSize = 28.sp
                )
            }

            is Result.Success -> {
                VitalCard(
                    title = "Status",
                    value = healthStatusState.data?.status,
                    imageIcon = HealthStatusIcon,
                    colorStops = VitalColors.PurpleGradient,
                    isLoading = false,
                    isOnline = isOnline,
                    onClick = {},
                    valueTextSize = 28.sp
                )
            }

            is Result.Error -> {
                VitalCard(
                    title = "Internal Error",
                    value = healthStatusState.message,
                    imageIcon = Icons.Default.Warning,
                    colorStops = VitalColors.PurpleGradient,
                    isLoading = false,
                    isOnline = false,
                    onClick = {},
                    valueTextSize = 12.sp
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.onBackground)
                    .height(5.dp)
                    .weight(0.3f)
            )
            Text(
                text = "Vitals Data".uppercase(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFont,
                maxLines = 1,
                modifier = Modifier.weight(0.4f)
            )
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.onBackground)
                    .height(5.dp)
                    .weight(0.3f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            when (vitalsState) {
                is Result.Loading -> {
                    VitalCard(
                        title = "Body Temperature",
                        value = null,
                        imageIcon = BodyTemperatureIcon,
                        colorStops = VitalColors.PinkGradient,
                        isLoading = true,
                        isOnline = isOnline,
                        onClick = {},
                        unit = "°C"
                    )
                    VitalCard(
                        title = "Heart Rate",
                        value = null,
                        imageIcon = HeartrateIcon,
                        colorStops = VitalColors.RedGradient,
                        isLoading = true,
                        isOnline = isOnline,
                        onClick = {},
                        unit = " BPM",
                    )
                    VitalCard(
                        title = "Sp02",
                        value = null,
                        imageIcon = SpO2Icon,
                        colorStops = VitalColors.BlueGradient,
                        isLoading = true,
                        isOnline = isOnline,
                        onClick = {},
                        unit = "%",
                    )
                }

                is Result.Success -> {
                    VitalCard(
                        title = "Body Temperature",
                        value = vitalsState.data?.temperature,
                        imageIcon = BodyTemperatureIcon,
                        colorStops = VitalColors.PinkGradient,
                        isLoading = false,
                        isOnline = isOnline,
                        onClick = {},
                        unit = "°C"
                    )
                    VitalCard(
                        title = "Heart Rate",
                        value = vitalsState.data?.heartrate,
                        imageIcon = HeartrateIcon,
                        colorStops = VitalColors.RedGradient,
                        isLoading = false,
                        isOnline = isOnline,
                        onClick = {},
                        unit = " BPM",
                    )
                    VitalCard(
                        title = "Sp02",
                        value = vitalsState.data?.oxygenSaturation,
                        imageIcon = SpO2Icon,
                        colorStops = VitalColors.BlueGradient,
                        isLoading = false,
                        isOnline = isOnline,
                        onClick = {},
                        unit = "%",
                    )
                }

                is Result.Error -> {
                    VitalCard(
                        title = "Internal Error",
                        value = vitalsState.message,
                        imageIcon = Icons.Default.Warning,
                        colorStops = VitalColors.PinkGradient,
                        isLoading = false,
                        isOnline = false,
                        onClick = {},
                        valueTextSize = 12.sp
                    )
                    VitalCard(
                        title = "Internal Error",
                        value = vitalsState.message,
                        imageIcon = Icons.Default.Warning,
                        colorStops = VitalColors.RedGradient,
                        isLoading = false,
                        isOnline = false,
                        onClick = {},
                        valueTextSize = 12.sp
                    )
                    VitalCard(
                        title = "Internal Error",
                        value = vitalsState.message,
                        imageIcon = Icons.Default.Warning,
                        colorStops = VitalColors.BlueGradient,
                        isLoading = false,
                        isOnline = false,
                        onClick = {},
                        valueTextSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = PIXEL_9)
@Composable
private fun DashboardBottomSheetPreview() {
    MonjaTheme {
        val sheetState = rememberModalBottomSheetState()
        val vital = Vitals(
            temperature = 32.7,
            heartrate = 87,
            oxygenSaturation = 99
        )
        val status = HealthStatus("Severe")

        DashboardBottomSheet(
            sheetState = sheetState,
            vitalsState = Result.Success(vital),
            healthStatusState = Result.Success(status),
            isOnline = true,
            onDismissSheetState = {}
        )
    }
}