package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val GPSCenterIcon: ImageVector
    get() {
        if (_GPSCenterIcon != null) {
            return _GPSCenterIcon!!
        }
        _GPSCenterIcon = ImageVector.Builder(
            name = "GPSCenterIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 469.33f,
            viewportHeight = 469.33f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(234.67f, 149.33f)
                curveToRelative(-47.15f, 0f, -85.33f, 38.19f, -85.33f, 85.33f)
                reflectiveCurveTo(187.52f, 320f, 234.67f, 320f)
                reflectiveCurveTo(320f, 281.81f, 320f, 234.67f)
                reflectiveCurveToRelative(-38.19f, -85.33f, -85.33f, -85.33f)
                close()
                moveTo(425.39f, 213.33f)
                curveTo(415.57f, 124.37f, 344.96f, 53.76f, 256f, 43.95f)
                lineTo(256f, 0f)
                horizontalLineToRelative(-42.67f)
                verticalLineToRelative(43.95f)
                curveTo(124.37f, 53.76f, 53.76f, 124.37f, 43.95f, 213.33f)
                lineTo(0f, 213.33f)
                lineTo(0f, 256f)
                horizontalLineToRelative(43.95f)
                curveToRelative(9.81f, 88.96f, 80.43f, 159.57f, 169.39f, 169.39f)
                verticalLineToRelative(43.95f)
                lineTo(256f, 469.33f)
                verticalLineToRelative(-43.95f)
                curveTo(344.96f, 415.57f, 415.57f, 344.96f, 425.39f, 256f)
                horizontalLineToRelative(43.95f)
                verticalLineToRelative(-42.67f)
                horizontalLineToRelative(-43.95f)
                close()
                moveTo(234.67f, 384f)
                curveToRelative(-82.45f, 0f, -149.33f, -66.88f, -149.33f, -149.33f)
                reflectiveCurveToRelative(66.88f, -149.33f, 149.33f, -149.33f)
                reflectiveCurveTo(384f, 152.21f, 384f, 234.67f)
                reflectiveCurveTo(317.12f, 384f, 234.67f, 384f)
                close()
            }
        }.build()

        return _GPSCenterIcon!!
    }

@Suppress("ObjectPropertyName")
private var _GPSCenterIcon: ImageVector? = null