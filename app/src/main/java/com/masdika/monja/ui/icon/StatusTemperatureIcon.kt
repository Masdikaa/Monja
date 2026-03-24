package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StatusTemperatureIcon: ImageVector
    get() {
        if (_StatusTemperatureIcon != null) {
            return _StatusTemperatureIcon!!
        }
        _StatusTemperatureIcon = ImageVector.Builder(
            name = "StatusTemperatureIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(338.31f, 263.51f)
                verticalLineTo(82.31f)
                arcTo(82.31f, 82.31f, 0f, isMoreThanHalf = false, isPositiveArc = false, 256f, 0f)
                arcToRelative(
                    82.31f,
                    82.31f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -82.31f,
                    82.31f
                )
                verticalLineToRelative(181.2f)
                arcToRelative(
                    137.69f,
                    137.69f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -55.56f,
                    110.67f
                )
                curveToRelative(0f, 75.43f, 61.46f, 137.29f, 136.88f, 137.82f)
                horizontalLineToRelative(1f)
                arcToRelative(
                    137.88f,
                    137.88f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    82.31f,
                    -248.49f
                )
                close()
                moveTo(256f, 474f)
                horizontalLineToRelative(-0.72f)
                arcToRelative(
                    99.93f,
                    99.93f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -88.22f,
                    -145.37f
                )
                arcTo(
                    101.07f,
                    101.07f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    196.4f,
                    294f
                )
                lineToRelative(15.29f, -11.39f)
                verticalLineTo(82.31f)
                arcToRelative(
                    44.31f,
                    44.31f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    88.62f,
                    0f
                )
                verticalLineToRelative(200.28f)
                lineTo(315.6f, 294f)
                arcToRelative(
                    101f,
                    101f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    29.33f,
                    34.64f
                )
                arcToRelative(
                    98.68f,
                    98.68f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    10.94f,
                    45.51f
                )
                arcTo(100f, 100f, 0f, isMoreThanHalf = false, isPositiveArc = true, 256f, 474f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(274f, 304.6f)
                verticalLineTo(150.14f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = false, isPositiveArc = false, -36f, 0f)
                verticalLineTo(304.6f)
                arcToRelative(
                    71.83f,
                    71.83f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    36f,
                    0f
                )
                close()
                moveTo(378.68f, 100f)
                horizontalLineTo(451f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -36f)
                horizontalLineToRelative(-72.32f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 36f)
                close()
                moveTo(451f, 218.26f)
                horizontalLineToRelative(-72.32f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 36f)
                horizontalLineTo(451f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -36f)
                close()
                moveTo(378.68f, 177.14f)
                horizontalLineToRelative(36.17f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -36f)
                horizontalLineToRelative(-36.17f)
                arcToRelative(18f, 18f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 36f)
                close()
            }
        }.build()

        return _StatusTemperatureIcon!!
    }

@Suppress("ObjectPropertyName")
private var _StatusTemperatureIcon: ImageVector? = null