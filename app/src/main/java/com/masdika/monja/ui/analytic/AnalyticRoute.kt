package com.masdika.monja.ui.analytic

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class AnalyticRoute(
    val macAddress: String,
    val vitalType: String
)

fun NavGraphBuilder.analyticScreenRoute() {
    composable<AnalyticRoute> {
        val viewModel = hiltViewModel<AnalyticViewModel>()
        AnalyticScreen(
            viewModel = viewModel
        )
    }
}