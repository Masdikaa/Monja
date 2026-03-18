package com.masdika.monja.ui.dashboard.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masdika.monja.data.model.Vitals
import com.masdika.monja.ui.icon.BodyTemperatureIcon
import com.masdika.monja.ui.icon.HeartrateIcon
import com.masdika.monja.ui.icon.SpO2Icon
import com.masdika.monja.ui.theme.MonjaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBottomSheet(
    sheetState: SheetState,
    vitals: Vitals?,
    isLoading: Boolean,
    onDismissSheetState: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissSheetState() },
        sheetState = sheetState
    ) {
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
                isLoading = isLoading,
                unit = "°C"
            )
            VitalCard(
                title = "Heart Rate",
                value = vitals?.heartrate,
                imageIcon = HeartrateIcon,
                colorStops = VitalColors.RedGradient,
                isLoading = isLoading,
                unit = " BPM",
            )
            VitalCard(
                title = "Sp02",
                value = vitals?.oxygenSaturation,
                imageIcon = SpO2Icon,
                colorStops = VitalColors.BlueGradient,
                isLoading = isLoading,
                unit = "%",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DashboardBottomSheetPreview() {
    MonjaTheme {
        val sheetState = rememberModalBottomSheetState()
        val vital = Vitals(
            temperature = 32.7,
            heartrate = 87,
            oxygenSaturation = 99
        )
        DashboardBottomSheet(
            sheetState = sheetState,
            vitals = vital,
            isLoading = false,
            onDismissSheetState = {}
        )
    }
}