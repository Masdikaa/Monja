package com.masdika.monja.ui.analytic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.masdika.monja.ui.component.MainTopAppBar

@Composable
fun AnalyticScreen(
    viewModel: AnalyticViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            Text("Analytic Screen\nDevice: ${viewModel.macAddress}\nVital: ${viewModel.vitalType}")
        }
    }
}