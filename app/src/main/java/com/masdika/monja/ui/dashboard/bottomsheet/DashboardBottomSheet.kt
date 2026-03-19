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
    vitals: Vitals?,
    healthStatus: HealthStatus?,
    isVitalsLoading: Boolean,
    isHealthStatusLoading: Boolean,
    onDismissSheetState: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissSheetState() },
        sheetState = sheetState
    ) {
        VitalCard(
            title = "Status",
            value = healthStatus?.status,
            imageIcon = HealthStatusIcon,
            colorStops = VitalColors.PurpleGradient,
            isLoading = isHealthStatusLoading,
            valueTextSize = 28.sp
        )
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
            VitalCard(
                title = "Body Temperature",
                value = vitals?.temperature,
                imageIcon = BodyTemperatureIcon,
                colorStops = VitalColors.PinkGradient,
                isLoading = isVitalsLoading,
                unit = "°C"
            )
            VitalCard(
                title = "Heart Rate",
                value = vitals?.heartrate,
                imageIcon = HeartrateIcon,
                colorStops = VitalColors.RedGradient,
                isLoading = isVitalsLoading,
                unit = " BPM",
            )
            VitalCard(
                title = "Sp02",
                value = vitals?.oxygenSaturation,
                imageIcon = SpO2Icon,
                colorStops = VitalColors.BlueGradient,
                isLoading = isVitalsLoading,
                unit = "%",
            )
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
            vitals = vital,
            healthStatus = status,
            isHealthStatusLoading = false,
            isVitalsLoading = false,
            onDismissSheetState = {}
        )
    }
}