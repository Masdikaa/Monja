package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ArrowUpIcon: ImageVector
    get() {
        if (_ArrowUpIcon != null) {
            return _ArrowUpIcon!!
        }
        _ArrowUpIcon = ImageVector.Builder(
            name = "ArrowUpIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 492f,
            viewportHeight = 492f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(484.14f, 328.47f)
                lineTo(264.99f, 109.33f)
                curveToRelative(-5.06f, -5.06f, -11.82f, -7.84f, -19.17f, -7.84f)
                curveToRelative(-7.21f, 0f, -13.96f, 2.78f, -19.02f, 7.84f)
                lineTo(7.85f, 328.27f)
                curveTo(2.79f, 333.33f, 0f, 340.09f, 0f, 347.3f)
                reflectiveCurveToRelative(2.78f, 13.97f, 7.85f, 19.03f)
                lineToRelative(16.12f, 16.12f)
                curveToRelative(5.06f, 5.06f, 11.82f, 7.86f, 19.03f, 7.86f)
                reflectiveCurveToRelative(13.96f, -2.8f, 19.03f, -7.86f)
                lineToRelative(183.85f, -183.85f)
                lineToRelative(184.06f, 184.06f)
                curveToRelative(5.06f, 5.06f, 11.82f, 7.85f, 19.03f, 7.85f)
                curveToRelative(7.21f, 0f, 13.96f, -2.79f, 19.03f, -7.85f)
                lineToRelative(16.13f, -16.13f)
                curveToRelative(10.49f, -10.49f, 10.49f, -27.57f, 0f, -38.06f)
                close()
            }
        }.build()

        return _ArrowUpIcon!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowUpIcon: ImageVector? = null
