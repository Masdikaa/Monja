package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PinDropIcon: ImageVector
    get() {
        if (_PinDropIcon != null) {
            return _PinDropIcon!!
        }
        _PinDropIcon = ImageVector.Builder(
            name = "PinDropIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(fill = SolidColor(Color(0xFFFD003A))) {
                moveTo(256f, 0f)
                curveTo(156.7f, 0f, 76f, 80.7f, 76f, 180f)
                curveToRelative(0f, 33.6f, 9.3f, 66.3f, 27f, 94.5f)
                lineToRelative(140.8f, 230.41f)
                curveToRelative(2.4f, 3.9f, 6f, 6.3f, 10.2f, 6.9f)
                curveToRelative(5.7f, 0.9f, 12f, -1.5f, 15.3f, -7.2f)
                lineToRelative(141.2f, -232.52f)
                curveTo(427.3f, 244.5f, 436f, 212.4f, 436f, 180f)
                curveTo(436f, 80.7f, 355.3f, 0f, 256f, 0f)
                close()
                moveTo(256f, 270f)
                curveToRelative(-50.4f, 0f, -90f, -40.8f, -90f, -90f)
                curveToRelative(0f, -49.5f, 40.5f, -90f, 90f, -90f)
                reflectiveCurveToRelative(90f, 40.5f, 90f, 90f)
                curveToRelative(0f, 48.9f, -39f, 90f, -90f, 90f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE50027))) {
                moveTo(256f, 0f)
                verticalLineToRelative(90f)
                curveToRelative(49.5f, 0f, 90f, 40.5f, 90f, 90f)
                curveToRelative(0f, 48.9f, -39f, 90f, -90f, 90f)
                verticalLineToRelative(241.99f)
                curveToRelative(5.12f, 0.12f, 10.38f, -2.34f, 13.3f, -7.38f)
                lineTo(410.5f, 272.1f)
                curveToRelative(16.8f, -27.6f, 25.5f, -59.7f, 25.5f, -92.1f)
                curveTo(436f, 80.7f, 355.3f, 0f, 256f, 0f)
                close()
            }
        }.build()

        return _PinDropIcon!!
    }

@Suppress("ObjectPropertyName")
private var _PinDropIcon: ImageVector? = null