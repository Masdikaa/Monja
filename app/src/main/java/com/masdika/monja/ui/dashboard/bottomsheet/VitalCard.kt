package com.masdika.monja.ui.dashboard.bottomsheet

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masdika.monja.ui.component.chart.ChartConfig
import com.masdika.monja.ui.component.chart.DataPoint
import com.masdika.monja.ui.component.chart.LineChart
import com.masdika.monja.ui.icon.HealthStatusIcon
import com.masdika.monja.ui.theme.MonjaTheme
import com.masdika.monja.ui.theme.openSansFont
import com.masdika.monja.ui.theme.poppinsFont

@Composable
fun VitalCard(
    title: String,
    value: Any?,
    chartData: List<DataPoint> = emptyList(),
    imageIcon: ImageVector,
    colorStops: Array<Pair<Float, Color>>,
    isLoading: Boolean,
    isOnline: Boolean,
    onClick: () -> Unit,
    unit: String = "",
    valueTextSize: TextUnit = 32.sp
) {
    val boxHeight = with(LocalDensity.current) { valueTextSize.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .background(Brush.horizontalGradient(colorStops = colorStops))
    ) {

        if (chartData.isNotEmpty() && !isLoading && isOnline) {
            LineChart(
                dataPoint = chartData,
                config = ChartConfig(
                    lineColor = Color.White.copy(0.3f),
                    showIndicators = false,
                    showDots = false,
                    showXAxisLabels = false,
                    showTooltip = false,
                    showShadow = false,
                    backgroundColor = Color.Transparent,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(0.25f)
            ) {
                Image(
                    imageVector = imageIcon,
                    contentDescription = null,
                    modifier = Modifier.size(65.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.75f)
                    .height(70.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontFamily = openSansFont,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!isLoading) {
                    val displayUnit = unit.ifEmpty { "" }
                    Text(
                        text = if (isOnline) "${value ?: "--"}$displayUnit" else "--",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFont,
                        color = Color.White,
                        fontSize = valueTextSize,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(boxHeight)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim, y = translateAnim)
        )
    )
}

@Preview
@Composable
private fun TemperatureCardPreview() {
    MonjaTheme {
        VitalCard(
            title = "Temperature",
            value = 32.0,
            imageIcon = HealthStatusIcon,
            colorStops = VitalColors.StatusGradient,
            isLoading = false,
            isOnline = false,
            onClick = {},
            unit = "°C"
        )
    }
}