package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val HealthStatusIcon: ImageVector
    get() {
        if (_HealthStatusIcon != null) {
            return _HealthStatusIcon!!
        }
        _HealthStatusIcon = ImageVector.Builder(
            name = "HealthStatusIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 130f,
            viewportHeight = 130f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(15f, 15f)
                    horizontalLineToRelative(100f)
                    verticalLineToRelative(100f)
                    horizontalLineTo(15f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFFD6F2FA))) {
                    moveTo(78.2f, 29.21f)
                    curveToRelative(7.27f, 3.28f, 14.54f, 8f, 19.77f, 14.92f)
                    reflectiveCurveToRelative(8.41f, 16.13f, 5.88f, 23.58f)
                    reflectiveCurveToRelative(-10.77f, 13.11f, -17.93f, 17.6f)
                    reflectiveCurveToRelative(-13.29f, 7.81f, -20.47f, 12f)
                    reflectiveCurveToRelative(-15.45f, 9.14f, -22.95f, 8.42f)
                    reflectiveCurveToRelative(-14.27f, -7.2f, -16.4f, -14.83f)
                    reflectiveCurveToRelative(0.4f, -16.39f, 0.9f, -23.9f)
                    reflectiveCurveToRelative(-1.15f, -13.69f, -0.13f, -19.81f)
                    arcToRelative(
                        27f,
                        27f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        10.1f,
                        -16.32f
                    )
                    arcToRelative(
                        33.72f,
                        33.72f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        19.54f,
                        -6.67f
                    )
                    curveToRelative(7.14f, -0.2f, 14.4f, 1.71f, 21.69f, 5.01f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFF3FDFF))) {
                    moveTo(31.56f, 17.44f)
                    arcTo(
                        1.51f,
                        1.51f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        30.5f,
                        17f
                    )
                    arcToRelative(
                        1.5f,
                        1.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -1.5f,
                        1.5f
                    )
                    verticalLineToRelative(7f)
                    arcToRelative(
                        1.5f,
                        1.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        3f,
                        0f
                    )
                    verticalLineToRelative(-7f)
                    arcToRelative(
                        1.52f,
                        1.52f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -0.44f,
                        -1.06f
                    )
                    close()
                    moveTo(48.56f, 17.44f)
                    arcTo(
                        1.51f,
                        1.51f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        47.5f,
                        17f
                    )
                    arcToRelative(
                        1.5f,
                        1.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -1.5f,
                        1.5f
                    )
                    verticalLineToRelative(7f)
                    arcToRelative(
                        1.5f,
                        1.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        3f,
                        0f
                    )
                    verticalLineToRelative(-7f)
                    arcToRelative(
                        1.52f,
                        1.52f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -0.44f,
                        -1.06f
                    )
                    close()
                    moveTo(101f, 80f)
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = false,
                        12f,
                        12f
                    )
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -12f,
                        -12f
                    )
                    close()
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(104f, 78.32f)
                    lineTo(104f, 68f)
                    arcToRelative(
                        17f,
                        17f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -34f,
                        0f
                    )
                    verticalLineToRelative(27f)
                    arcToRelative(
                        14f,
                        14f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -28f,
                        0f
                    )
                    lineTo(42f, 76.8f)
                    arcTo(
                        24.13f,
                        24.13f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        63f,
                        53f
                    )
                    lineTo(63f, 28f)
                    arcToRelative(
                        10f,
                        10f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -10f,
                        -10f
                    )
                    horizontalLineToRelative(-2f)
                    arcToRelative(
                        3.43f,
                        3.43f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -1f,
                        -2f
                    )
                    arcToRelative(
                        3.51f,
                        3.51f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -6f,
                        2.48f
                    )
                    verticalLineToRelative(7f)
                    arcToRelative(
                        3.5f,
                        3.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        7f,
                        0.5f
                    )
                    horizontalLineToRelative(2f)
                    arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
                    lineTo(55f, 53f)
                    arcToRelative(
                        16f,
                        16f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        -32f,
                        0f
                    )
                    lineTo(23f, 28f)
                    arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, -2f)
                    horizontalLineToRelative(2f)
                    arcToRelative(
                        3.5f,
                        3.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        7f,
                        -0.5f
                    )
                    verticalLineToRelative(-7f)
                    arcToRelative(
                        3.5f,
                        3.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -7f,
                        -0.5f
                    )
                    horizontalLineToRelative(-2f)
                    arcToRelative(
                        10f,
                        10f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -10f,
                        10f
                    )
                    verticalLineToRelative(25f)
                    arcToRelative(
                        24.13f,
                        24.13f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        21f,
                        23.8f
                    )
                    lineTo(36f, 95f)
                    arcToRelative(
                        20f,
                        20f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = false,
                        40f,
                        0f
                    )
                    lineTo(76f, 68f)
                    arcToRelative(
                        11f,
                        11f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        22f,
                        0f
                    )
                    verticalLineToRelative(10.3f)
                    arcToRelative(
                        14f,
                        14f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = false,
                        6f,
                        0f
                    )
                    close()
                    moveTo(49f, 25.51f)
                    arcToRelative(
                        1.5f,
                        1.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        -3f,
                        0f
                    )
                    verticalLineToRelative(-7f)
                    arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 47.5f, 17f)
                    arcToRelative(
                        1.49f,
                        1.49f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        1.5f,
                        1.5f
                    )
                    close()
                    moveTo(29f, 18.51f)
                    arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 30.5f, 17f)
                    arcToRelative(
                        1.49f,
                        1.49f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        1.5f,
                        1.5f
                    )
                    verticalLineToRelative(7f)
                    arcToRelative(
                        1.5f,
                        1.5f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        -3f,
                        0f
                    )
                    close()
                    moveTo(101f, 104f)
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        12f,
                        -12f
                    )
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        -12f,
                        12f
                    )
                    close()
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(101f, 83.68f)
                    arcToRelative(
                        8.32f,
                        8.32f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = false,
                        8.31f,
                        8.32f
                    )
                    arcToRelative(
                        8.32f,
                        8.32f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = false,
                        -8.31f,
                        -8.32f
                    )
                    close()
                    moveTo(101f, 97.31f)
                    arcToRelative(
                        5.32f,
                        5.32f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        5.31f,
                        -5.31f
                    )
                    arcToRelative(
                        5.31f,
                        5.31f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        -5.31f,
                        5.31f
                    )
                    close()
                    moveTo(29f, 98f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 1f)
                    horizontalLineToRelative(-3f)
                    verticalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 0f)
                    verticalLineToRelative(-3f)
                    horizontalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -2f)
                    horizontalLineToRelative(3f)
                    verticalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 0f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                    close()
                    moveTo(49f, 40f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 1f)
                    horizontalLineToRelative(-3f)
                    verticalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 0f)
                    verticalLineToRelative(-3f)
                    horizontalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -2f)
                    horizontalLineToRelative(3f)
                    verticalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 0f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF0DB60D))) {
                    moveTo(57f, 91f)
                    moveToRelative(-4f, 0f)
                    arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 8f, 0f)
                    arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, -8f, 0f)
                }
                path(fill = SolidColor(Color(0xFF0DB60D))) {
                    moveTo(34f, 53f)
                    moveToRelative(-4f, 0f)
                    arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 8f, 0f)
                    arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, -8f, 0f)
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(57.94f, 80.69f)
                    moveToRelative(-1.19f, 0f)
                    arcToRelative(
                        1.19f,
                        1.19f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        2.38f,
                        0f
                    )
                    arcToRelative(
                        1.19f,
                        1.19f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -2.38f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(69.94f, 44.19f)
                    moveToRelative(-1.81f, 0f)
                    arcToRelative(
                        1.81f,
                        1.81f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        3.62f,
                        0f
                    )
                    arcToRelative(
                        1.81f,
                        1.81f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -3.62f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFF0DB60D))) {
                    moveTo(98.88f, 41.13f)
                    moveToRelative(-2.88f, 0f)
                    arcToRelative(
                        2.88f,
                        2.88f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        5.76f,
                        0f
                    )
                    arcToRelative(
                        2.88f,
                        2.88f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -5.76f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(99.81f, 29.06f)
                    moveToRelative(-1.19f, 0f)
                    arcToRelative(
                        1.19f,
                        1.19f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        2.38f,
                        0f
                    )
                    arcToRelative(
                        1.19f,
                        1.19f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -2.38f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(81f, 106.63f)
                    moveToRelative(-1.75f, 0f)
                    arcToRelative(
                        1.75f,
                        1.75f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        3.5f,
                        0f
                    )
                    arcToRelative(
                        1.75f,
                        1.75f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -3.5f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(22.13f, 81.75f)
                    moveToRelative(-1.63f, 0f)
                    arcToRelative(
                        1.63f,
                        1.63f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        3.26f,
                        0f
                    )
                    arcToRelative(
                        1.63f,
                        1.63f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -3.26f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(110.13f, 20.13f)
                    moveToRelative(-1.63f, 0f)
                    arcToRelative(
                        1.63f,
                        1.63f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        3.26f,
                        0f
                    )
                    arcToRelative(
                        1.63f,
                        1.63f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -3.26f,
                        0f
                    )
                }
                path(fill = SolidColor(Color(0xFFAA33E5))) {
                    moveTo(91.67f, 71f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 1f)
                    horizontalLineToRelative(-3f)
                    verticalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 0f)
                    verticalLineToRelative(-3f)
                    horizontalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -2f)
                    horizontalLineToRelative(3f)
                    verticalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 0f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF0DB60D))) {
                    moveTo(90f, 31f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 1f)
                    horizontalLineToRelative(-4f)
                    verticalLineToRelative(4f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 0f)
                    verticalLineToRelative(-4f)
                    horizontalLineToRelative(-4f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -2f)
                    horizontalLineToRelative(4f)
                    verticalLineToRelative(-4f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2f, 0f)
                    verticalLineToRelative(4f)
                    horizontalLineToRelative(4f)
                    arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                    close()
                }
            }
        }.build()

        return _HealthStatusIcon!!
    }

@Suppress("ObjectPropertyName")
private var _HealthStatusIcon: ImageVector? = null