package com.masdika.monja.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masdika.monja.R
import com.masdika.monja.ui.icon.MonjaIcon
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.poppinsFont
import com.masdika.monja.util.UiText

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SplashEvent.NavigateToDashboard -> onNavigateToDashboard()
                is SplashEvent.RetryInitialization -> {/* Handled by ViewModel */
                }
            }
        }
    }

    SplashContent(
        state = state,
        onRetryClick = { viewModel.retry() }
    )
}

@Composable
fun SplashContent(
    state: SplashState,
    onRetryClick: () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "logo_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "logo_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = MonjaIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale)
            )
            Text(
                text = stringResource(R.string.app_name).uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(32.dp))
            when (state.initializationState) {
                is InitializationState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = state.statusMessage.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                }

                is InitializationState.Success -> {
                    /* Navigate To Dashboard */
                }

                is InitializationState.Error -> {
                    Text(
                        text = state.initializationState.message.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onRetryClick) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Splash Screen Error Preview",
    device = PIXEL_9
)
@Composable
private fun SplashScreenErrorPreview() {
    MonjaTheme {
        SplashContent(
            state = SplashState(
                initializationState = InitializationState.Error(UiText.DynamicString("Preview Error")),
                statusMessage = UiText.DynamicString("Preview Status Message"),
                retryCount = 3
            ),
            onRetryClick = {},
        )
    }
}

@Preview(
    name = "Splash Screen Loading Preview",
    device = PIXEL_9
)
@Composable
private fun SplashScreenLoadingPreview() {
    MonjaTheme {
        SplashContent(
            state = SplashState(
                initializationState = InitializationState.Loading,
                statusMessage = UiText.DynamicString("Preview Status Message"),
                retryCount = 3
            ),
            onRetryClick = {}
        )
    }
}