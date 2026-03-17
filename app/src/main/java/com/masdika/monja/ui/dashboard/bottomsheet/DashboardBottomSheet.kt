package com.masdika.monja.ui.dashboard.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masdika.monja.data.model.Vitals

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Column() {
                Text("Heart Rate: ${vitals?.heartrate ?: "--"}")
                Text("Temperature: ${vitals?.temperature ?: "--"}")
                Text("SpO2: ${vitals?.oxygenSaturation ?: "--"}")
            }
        }
    }
}