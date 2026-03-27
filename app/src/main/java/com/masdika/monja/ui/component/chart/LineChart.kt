package com.masdika.monja.ui.component.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.hypot

@Composable
fun LineChart(
    dataPoint: List<DataPoint>,
    modifier: Modifier = Modifier,
    config: ChartConfig = ChartConfig()
) {
    val textMeasurer = rememberTextMeasurer()

    var selectedDataPoint by remember { mutableStateOf<DataPoint?>(null) }
    var selectedPointOffset by remember { mutableStateOf<Offset?>(null) }

    val actualYMin = config.yAxisMin ?: dataPoint.minOfOrNull { it.value } ?: 0.0
    val actualYMax = config.yAxisMax ?: dataPoint.maxOfOrNull { it.value } ?: 100.0

    val paddingStart = if (config.showIndicators) 50.dp else 0.dp
    val paddingEnd = if (config.showIndicators) 20.dp else 0.dp
    val paddingTop = if (config.showIndicators) 40.dp else 16.dp
    val paddingBottom = if (config.showIndicators) 40.dp else 16.dp

    Canvas(
        modifier = modifier
            .padding(
                start = paddingStart,
                top = paddingTop,
                end = paddingEnd,
                bottom = paddingBottom
            )
            .pointerInput(dataPoint, config, actualYMin, actualYMax) {
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
                    val valueRange = (actualYMax - actualYMin).coerceAtLeast(1.0).toFloat()

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
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        if (config.showIndicators) {
            drawYAxis(
                scope = this,
                textMeasurer = textMeasurer,
                config = config,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                actualYMin = actualYMin,
                actualYMax = actualYMax
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
                canvasHeight = canvasHeight
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

            selectedPointOffset?.let { offset ->
                drawCircle(
                    color = config.lineColor.copy(alpha = 0.3f),
                    radius = 12.dp.toPx(),
                    center = offset
                )
            }
        }

        if (selectedDataPoint != null && selectedPointOffset != null) {
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

private fun drawYAxis(
    scope: DrawScope,
    textMeasurer: TextMeasurer,
    config: ChartConfig,
    canvasWidth: Float,
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
                    -(valueLabelLayoutResult.size.width.toFloat() + 8.dp.toPx()),
                    yOffset - (valueLabelLayoutResult.size.height / 2f)
                )
            )

            drawLine(
                color = config.indicatorColor,
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
    canvasHeight: Float
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
                    val timeText =
                        closestDataPoint.labelX ?: formatter.format(closestDataPoint.timestamp)
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

private fun drawTooltip(
    scope: DrawScope,
    textMeasurer: TextMeasurer,
    dataPoint: DataPoint,
    config: ChartConfig,
    center: Offset
) {
    with(scope) {
        val text = "${dataPoint.value}"
        val textStyle = TextStyle(color = Color.White, fontSize = 14.sp)
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