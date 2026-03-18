package com.masdika.monja.ui.icon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val HeartrateIcon: ImageVector
    get() {
        if (_HeartrateIcon != null) {
            return _HeartrateIcon!!
        }
        _HeartrateIcon = ImageVector.Builder(
            name = "HeartrateIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.083f to Color(0xFFFF6A6A),
                        1f to Color(0xFFF72257)
                    ),
                    start = Offset(0f, 1.091f),
                    end = Offset(21.72f, 24.982f)
                ),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveToRelative(11.553f, 21.894f)
                lineToRelative(-0.012f, -0.005f)
                lineToRelative(0.01f, 0.005f)
                lineToRelative(-0.002f, -0.001f)
                lineToRelative(-0.008f, -0.004f)
                lineToRelative(-0.026f, -0.014f)
                arcToRelative(17.142f, 17.142f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.43f, -0.23f)
                arcToRelative(28.105f, 28.105f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4.745f, -3.33f)
                curveToRelative(-2.558f, -2.24f, -5.34f, -5.619f, -5.34f, -9.815f)
                curveToRelative(0f, -1.43f, 0.425f, -3.029f, 1.393f, -4.295f)
                curveTo(3.386f, 2.907f, 4.924f, 2f, 7f, 2f)
                curveToRelative(2.063f, 0f, 3.544f, 0.891f, 4.49f, 1.765f)
                curveToRelative(0.191f, 0.176f, 0.36f, 0.351f, 0.51f, 0.52f)
                curveToRelative(0.15f, -0.169f, 0.319f, -0.344f, 0.51f, -0.52f)
                curveTo(13.455f, 2.891f, 14.936f, 2f, 17f, 2f)
                curveToRelative(2.076f, 0f, 3.614f, 0.907f, 4.607f, 2.205f)
                curveTo(22.575f, 5.471f, 23f, 7.07f, 23f, 8.5f)
                curveToRelative(0f, 4.196f, -2.782f, 7.575f, -5.341f, 9.815f)
                arcToRelative(28.114f, 28.114f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.082f, 3.512f)
                lineToRelative(-0.092f, 0.048f)
                lineToRelative(-0.026f, 0.014f)
                lineToRelative(-0.008f, 0.003f)
                lineToRelative(-0.004f, 0.002f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.894f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF850026)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8f, 7f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.928f, 0.629f)
                lineTo(11f, 12.807f)
                lineToRelative(1.272f, -3.178f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.856f, 0f)
                lineTo(15.077f, 12f)
                horizontalLineTo(16f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 2f)
                horizontalLineToRelative(-1.6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.928f, -0.629f)
                lineToRelative(-0.272f, -0.678f)
                lineToRelative(-1.271f, 3.178f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.857f, 0f)
                lineTo(8f, 10.693f)
                lineTo(6.928f, 13.37f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 14f)
                horizontalLineTo(2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -2f)
                horizontalLineToRelative(3.323f)
                lineToRelative(1.749f, -4.371f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 7f)
                close()
            }
        }.build()

        return _HeartrateIcon!!
    }

@Suppress("ObjectPropertyName")
private var _HeartrateIcon: ImageVector? = null