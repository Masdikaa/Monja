package com.masdika.monja.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(state.test)
    }
}
