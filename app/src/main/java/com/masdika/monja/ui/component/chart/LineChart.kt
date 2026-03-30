package com.masdika.monja.ui.component.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.hypot

@Composable
fun LineChart(
    dataPoint: List<DataPoint>,
    modifier: Modifier = Modifier,
    config: ChartConfig = ChartConfig(),
    viewportDataPoints: Int = 0
) {
    val textMeasurer = rememberTextMeasurer()

    var selectedDataPoint by remember { mutableStateOf<DataPoint?>(null) }
    var selectedPointOffset by remember { mutableStateOf<Offset?>(null) }

    val actualYMin = config.yAxisMin ?: dataPoint.minOfOrNull { it.value } ?: 0.0
    val actualYMax = config.yAxisMax ?: dataPoint.maxOfOrNull { it.value } ?: 100.0

    BoxWithConstraints(modifier = modifier) {
        val availableWidthDp = maxWidth

        val yAxisWidthDp = if (config.showIndicators) 45.dp else 0.dp

        val chartViewportWidthDp = availableWidthDp - yAxisWidthDp

        val widthRatio = if (viewportDataPoints > 0) {
            (dataPoint.size.toFloat() / viewportDataPoints.toFloat()).coerceAtLeast(1.0f)
        } else {
            1.0f
        }

        val calculatedWidthDp = chartViewportWidthDp * widthRatio

        val scrollState = rememberScrollState()
        val isAtEndFrame by remember {
            derivedStateOf {
                scrollState.value >= (scrollState.maxValue - 20)
            }
        }

        LaunchedEffect(dataPoint.size, calculatedWidthDp) {
            if (calculatedWidthDp > chartViewportWidthDp) {
                scrollState.scrollTo(scrollState.maxValue)
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {

            if (config.showIndicators) {
                Canvas(
                    modifier = Modifier
                        .width(yAxisWidthDp)
                        .fillMaxHeight()
                        .padding(top = 16.dp, bottom = 40.dp)
                ) {
                    drawYAxis(
                        scope = this,
                        textMeasurer = textMeasurer,
                        config = config,
                        canvasHeight = size.height,
                        actualYMin = actualYMin,
                        actualYMax = actualYMax,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .horizontalScroll(scrollState)
                        .width(calculatedWidthDp)
                        .padding(top = 16.dp, bottom = 40.dp)
                        .then(
                            if (config.showTooltip) {
                                Modifier.pointerInput(dataPoint, config, actualYMin, actualYMax) {
                                    detectTapGestures { tapOffset ->
                                        if (dataPoint.isEmpty()) return@detectTapGestures

                                        val canvasWidth = size.width
                                        val canvasHeight = size.height

                                        val minTime = dataPoint.minOfOrNull {
                                            it.timestamp.toEpochMilli()
                                        } ?: return@detectTapGestures
                                        val maxTime = dataPoint.maxOfOrNull {
                                            it.timestamp.toEpochMilli()
                                        } ?: return@detectTapGestures

                                        val timeRange = (maxTime - minTime).coerceAtLeast(1L)
                                        val valueRange =
                                            (actualYMax - actualYMin).coerceAtLeast(1.0).toFloat()

                                        val touchTolerance = 24.dp.toPx()
                                        var pointFound = false

                                        for (point in dataPoint) {
                                            val x =
                                                ((point.timestamp.toEpochMilli() - minTime).toFloat() / timeRange) * canvasWidth
                                            val y =
                                                canvasHeight - (((point.value.toFloat() - actualYMin.toFloat()) / valueRange) * canvasHeight)
                                            val pointOffset = Offset(x, y)

                                            val distance = hypot(
                                                (tapOffset.x - pointOffset.x).toDouble(),
                                                (tapOffset.y - pointOffset.y).toDouble()
                                            )

                                            if (distance <= touchTolerance) {
                                                selectedDataPoint = point
                                                selectedPointOffset = pointOffset
                                                pointFound = true
                                                break
                                            }
                                        }

                                        if (!pointFound) {
                                            selectedDataPoint = null
                                            selectedPointOffset = null
                                        }
                                    }
                                }
                            } else Modifier
                        )
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    if (config.showIndicators) {
                        drawYAxisGridLines(
                            scope = this,
                            config = config,
                            canvasWidth = canvasWidth,
                            canvasHeight = canvasHeight
                        )

                        drawXAxisBase(
                            scope = this,
                            config = config,
                            canvasWidth = canvasWidth,
                            canvasHeight = canvasHeight
                        )

                        drawXAxisLabel(
                            scope = this,
                            textMeasurer = textMeasurer,
                            dataPoints = dataPoint,
                            config = config,
                            canvasWidth = canvasWidth,
                            canvasHeight = canvasHeight,
                            isAtEndFrame = isAtEndFrame,
                            viewportDataPoints = viewportDataPoints
                        )
                    }

                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = canvasWidth,
                        bottom = canvasHeight
                    ) {
                        drawLineAndPoint(
                            scope = this,
                            dataPoints = dataPoint,
                            config = config,
                            canvasWidth = canvasWidth,
                            canvasHeight = canvasHeight,
                            actualYMin = actualYMin,
                            actualYMax = actualYMax
                        )

                        if (config.showTooltip && selectedPointOffset != null) {
                            drawCircle(
                                color = config.lineColor.copy(alpha = 0.3f),
                                radius = 12.dp.toPx(),
                                center = selectedPointOffset!!
                            )
                        }
                    }

                    if (config.showTooltip && selectedDataPoint != null && selectedPointOffset != null) {
                        drawTooltip(
                            scope = this,
                            textMeasurer = textMeasurer,
                            dataPoint = selectedDataPoint!!,
                            config = config,
                            center = selectedPointOffset!!
                        )
                    }
                }
            }
        }
    }
}

private fun drawYAxis(
    scope: DrawScope,
    textMeasurer: TextMeasurer,
    config: ChartConfig,
    canvasHeight: Float,
    actualYMin: Double,
    actualYMax: Double
) {
    val numGridLines = 5
    val yValueStep = (actualYMax - actualYMin) / numGridLines
    val labelTextStyle = TextStyle(
        color = config.indicatorColor,
        fontSize = 12.sp,
        fontFamily = config.labelFontFamily
    )

    if (config.yAxisLabel.isNotEmpty()) {
        val yLabelLayoutResult = textMeasurer.measure(
            text = config.yAxisLabel,
            style = labelTextStyle.copy(fontSize = 14.sp)
        )

        with(scope) {
            val textX = -45.dp.toPx()
            val textY = (canvasHeight / 2f) - (yLabelLayoutResult.size.width / 2f)

            rotate(degrees = -90f, pivot = Offset(textX, textY)) {
                drawText(
                    textLayoutResult = yLabelLayoutResult,
                    color = config.indicatorColor,
                    topLeft = Offset(textX, textY)
                )
            }
        }
    }

    for (i in 0..numGridLines) {
        val value = (actualYMax - (i * yValueStep)).toInt()
        val yOffset = i * (canvasHeight / numGridLines)

        val valueLabelLayoutResult = textMeasurer.measure(
            text = value.toString(),
            style = labelTextStyle
        )

        with(scope) {
            drawText(
                textLayoutResult = valueLabelLayoutResult,
                color = config.indicatorColor,
                topLeft = Offset(
                    x = (valueLabelLayoutResult.size.width.toFloat() + 4.dp.toPx()),
                    y = yOffset - (valueLabelLayoutResult.size.height / 2f)
                )
            )
        }
    }
}

private fun drawYAxisGridLines(
    scope: DrawScope,
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float
) {
    val numGridLines = 5

    for (i in 0..numGridLines) {
        val yOffset = i * (canvasHeight / numGridLines)

        with(scope) {
            drawLine(
                color = config.indicatorColor.copy(0.5f),
                start = Offset(0f, yOffset),
                end = Offset(canvasWidth, yOffset),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

private fun drawXAxisBase(
    scope: DrawScope,
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float
) {
    with(scope) {
        drawLine(
            color = config.indicatorColor,
            start = Offset(0f, canvasHeight),
            end = Offset(canvasWidth, canvasHeight),
            strokeWidth = 2.dp.toPx()
        )
    }
}

private fun drawLineAndPoint(
    scope: DrawScope,
    dataPoints: List<DataPoint>,
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float,
    actualYMin: Double,
    actualYMax: Double
) {
    if (dataPoints.isEmpty()) return

    val minTime = dataPoints.minOfOrNull { it.timestamp.toEpochMilli() } ?: return
    val maxTime = dataPoints.maxOfOrNull { it.timestamp.toEpochMilli() } ?: return
    val timeRange = (maxTime - minTime).coerceAtLeast(1L)

    val valueRange = (actualYMax - actualYMin).coerceAtLeast(1.0).toFloat()

    val coordinates = dataPoints.map { point ->
        val x = ((point.timestamp.toEpochMilli() - minTime).toFloat() / timeRange) * canvasWidth
        val y =
            canvasHeight - (((point.value.toFloat() - actualYMin.toFloat()) / valueRange) * canvasHeight)
        Offset(x, y)
    }

    val path = Path()
    if (coordinates.isNotEmpty()) {
        path.moveTo(coordinates.first().x, coordinates.first().y)

        for (i in 0 until coordinates.size - 1) {
            val current = coordinates[i]
            val next = coordinates[i + 1]

            val controlX1 = current.x + (next.x - current.x) / 2f
            val controlX2 = current.x + (next.x - current.x) / 2f

            path.cubicTo(
                x1 = controlX1, y1 = current.y,
                x2 = controlX2, y2 = next.y,
                x3 = next.x, y3 = next.y
            )
        }

        with(scope) {
            drawPath(
                path = path,
                color = config.lineColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }

    if (config.showDots) {
        coordinates.forEach { offset ->
            with(scope) {
                drawCircle(
                    color = config.pointColor,
                    radius = 4.dp.toPx(),
                    center = offset
                )
            }
        }
    }
}

private fun drawXAxisLabel(
    scope: DrawScope,
    textMeasurer: TextMeasurer,
    dataPoints: List<DataPoint>,
    config: ChartConfig,
    canvasWidth: Float,
    canvasHeight: Float,
    isAtEndFrame: Boolean,
    viewportDataPoints: Int
) {
    if (!config.showXAxisLabels || dataPoints.isEmpty()) return

    val minTime = dataPoints.minOfOrNull { it.timestamp.toEpochMilli() } ?: return
    val maxTime = dataPoints.maxOfOrNull { it.timestamp.toEpochMilli() } ?: return
    val timeRange = (maxTime - minTime).coerceAtLeast(1L)

    val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())

    val labelTextStyle = TextStyle(
        color = config.indicatorColor,
        fontSize = 12.sp,
        fontFamily = config.labelFontFamily
    )

    with(scope) {
        val minSpacingPx = config.xAxisLabelSpacingDp.dp.toPx()
        val maxLabelsPossible = (canvasWidth / minSpacingPx).toInt().coerceAtLeast(2)
        val stepPx = canvasWidth / (maxLabelsPossible - 1).toFloat()
        var lastDrawnXPos = -minSpacingPx

        for (i in 0 until maxLabelsPossible) {
            val targetXPx = i * stepPx
            val targetTimeMilli = minTime + ((targetXPx / canvasWidth) * timeRange).toLong()

            val closestDataPoint = dataPoints.minByOrNull {
                abs(it.timestamp.toEpochMilli() - targetTimeMilli)
            }

            if (closestDataPoint != null) {
                val actualXPx =
                    ((closestDataPoint.timestamp.toEpochMilli() - minTime).toFloat() / timeRange) * canvasWidth

                if (actualXPx - lastDrawnXPos >= (minSpacingPx * 0.8f)) {
                    val timeText = if (isAtEndFrame) {
                        if (i == maxLabelsPossible - 2) {
                            if (viewportDataPoints == 60) {
                                "Last 1 hour"
                            } else {
                                "Last $viewportDataPoints data"
                            }
                        } else ""
                    } else {
                        closestDataPoint.labelX ?: formatter.format(closestDataPoint.timestamp)
                    }
                    if (timeText.isNotEmpty()) {
                        val textLayoutResult = textMeasurer.measure(
                            text = timeText,
                            style = labelTextStyle
                        )

                        drawText(
                            textLayoutResult = textLayoutResult,
                            color = config.indicatorColor,
                            topLeft = Offset(
                                x = actualXPx - (textLayoutResult.size.width / 2f),
                                y = canvasHeight + 8.dp.toPx()
                            )
                        )
                        lastDrawnXPos = actualXPx
                    }
                }
            }
        }
    }
}

private fun drawTooltip(
    scope: DrawScope,
    textMeasurer: TextMeasurer,
    dataPoint: DataPoint,
    config: ChartConfig,
    center: Offset
) {
    with(scope) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        val timeString = formatter.format(dataPoint.timestamp)

        val valueString = if (dataPoint.value % 1.0 == 0.0) {
            dataPoint.value.toInt().toString()
        } else {
            String.format(Locale.getDefault(), "%.1f", dataPoint)
        }

        val text = "$valueString\n$timeString"
        val textStyle = TextStyle(
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        val textLayoutResult = textMeasurer.measure(text = text, style = textStyle)

        val padding = 8.dp.toPx()
        val boxWidth = textLayoutResult.size.width + (padding * 2)
        val boxHeight = textLayoutResult.size.height + (padding * 2)

        val boxTopLeft = Offset(
            x = center.x - (boxWidth / 2f),
            y = center.y - boxHeight - 12.dp.toPx()
        )

        drawRoundRect(
            color = config.tooltipBackgroundColor,
            topLeft = boxTopLeft,
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        drawText(
            textLayoutResult = textLayoutResult,
            color = config.tooltipTextColor,
            topLeft = Offset(
                x = boxTopLeft.x + padding,
                y = boxTopLeft.y + padding
            )
        )
    }
}