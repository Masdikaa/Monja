package com.masdika.monja.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masdika.monja.ui.component.MainTopAppBar

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            MainTopAppBar(
                title = "History"
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(state.test)
        }
    }
}
