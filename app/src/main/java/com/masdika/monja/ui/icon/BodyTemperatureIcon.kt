package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BodyTemperatureIcon: ImageVector
    get() {
        if (_BodyTemperatureIcon != null) {
            return _BodyTemperatureIcon!!
        }
        _BodyTemperatureIcon = ImageVector.Builder(
            name = "BodyTemperatureIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 64f,
            viewportHeight = 64f
        ).apply {
            path(fill = SolidColor(Color(0xFFFF738E))) {
                moveTo(31.071f, 51.38f)
                curveToRelative(-0.576f, -5.588f, 2.394f, -10.54f, 6.929f, -12.906f)
                verticalLineTo(27.587f)
                arcTo(7.96f, 7.96f, 0f, isMoreThanHalf = false, isPositiveArc = false, 35f, 27f)
                horizontalLineTo(15f)
                arcToRelative(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, -8f, 8f)
                verticalLineToRelative(20f)
                horizontalLineToRelative(25.011f)
                arcToRelative(
                    12.696f,
                    12.696f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.94f,
                    -3.62f
                )
                close()
            }
            path(fill = SolidColor(Color(0xFFFF738E))) {
                moveTo(24f, 18f)
                moveToRelative(-9f, 0f)
                arcToRelative(9f, 9f, 0f, isMoreThanHalf = true, isPositiveArc = true, 18f, 0f)
                arcToRelative(9f, 9f, 0f, isMoreThanHalf = true, isPositiveArc = true, -18f, 0f)
            }
            path(fill = SolidColor(Color(0xFFDEDEE0))) {
                moveTo(50f, 38.474f)
                verticalLineTo(7f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = false, -12f, 0f)
                verticalLineToRelative(31.474f)
                curveToRelative(-4.535f, 2.366f, -7.505f, 7.318f, -6.929f, 12.906f)
                curveToRelative(0.618f, 5.994f, 5.455f, 10.874f, 11.445f, 11.537f)
                curveTo(50.36f, 63.786f, 57f, 57.668f, 57f, 50f)
                curveToRelative(0f, -5.014f, -2.843f, -9.357f, -7f, -11.526f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFF738E))) {
                moveTo(42.528f, 58.882f)
                curveToRelative(-3.617f, -0.576f, -6.584f, -3.401f, -7.331f, -6.986f)
                curveToRelative(-0.848f, -4.069f, 1.106f, -8.024f, 4.654f, -9.876f)
                lineTo(42f, 40.899f)
                verticalLineTo(7f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4f, 0f)
                verticalLineToRelative(33.899f)
                lineToRelative(2.149f, 1.121f)
                arcTo(8.98f, 8.98f, 0f, isMoreThanHalf = false, isPositiveArc = true, 53f, 50f)
                curveToRelative(0f, 5.446f, -4.861f, 9.777f, -10.472f, 8.882f)
                close()
            }
            path(fill = SolidColor(Color(0xFF003564))) {
                moveTo(6f, 35.001f)
                verticalLineToRelative(20f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, 1f)
                horizontalLineToRelative(24.348f)
                curveToRelative(2.034f, 4.27f, 6.178f, 7.372f, 11.057f, 7.912f)
                curveToRelative(0.521f, 0.058f, 1.041f, 0.086f, 1.559f, 0.086f)
                curveToRelative(3.449f, 0f, 6.797f, -1.26f, 9.371f, -3.565f)
                arcTo(
                    14.02f,
                    14.02f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    58f,
                    50.001f
                )
                arcToRelative(
                    13.96f,
                    13.96f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -7f,
                    -12.117f
                )
                lineTo(51f, 7.001f)
                curveToRelative(0f, -3.86f, -3.141f, -7f, -7f, -7f)
                reflectiveCurveToRelative(-7f, 3.14f, -7f, 7f)
                verticalLineToRelative(19.236f)
                arcToRelative(
                    8.95f,
                    8.95f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -2f,
                    -0.236f
                )
                horizontalLineToRelative(-5.026f)
                curveToRelative(2.438f, -1.825f, 4.026f, -4.727f, 4.026f, -8f)
                curveToRelative(0f, -5.514f, -4.486f, -10f, -10f, -10f)
                reflectiveCurveToRelative(-10f, 4.486f, -10f, 10f)
                curveToRelative(0f, 3.273f, 1.588f, 6.175f, 4.026f, 8f)
                lineTo(15f, 26.001f)
                curveToRelative(-4.963f, 0f, -9f, 4.038f, -9f, 9f)
                close()
                moveTo(39f, 7.001f)
                curveToRelative(0f, -2.757f, 2.243f, -5f, 5f, -5f)
                reflectiveCurveToRelative(5f, 2.243f, 5f, 5f)
                verticalLineToRelative(31.475f)
                curveToRelative(0f, 0.373f, 0.207f, 0.714f, 0.537f, 0.887f)
                arcTo(
                    11.97f,
                    11.97f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    56f,
                    50.001f
                )
                curveToRelative(0f, 3.408f, -1.457f, 6.668f, -3.998f, 8.943f)
                curveToRelative(-2.575f, 2.306f, -5.901f, 3.366f, -9.376f, 2.98f)
                curveToRelative(-4.367f, -0.484f, -8.039f, -3.366f, -9.687f, -7.279f)
                arcToRelative(
                    11.762f,
                    11.762f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.872f,
                    -3.366f
                )
                curveToRelative(-0.511f, -4.947f, 2f, -9.625f, 6.396f, -11.917f)
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    0.537f,
                    -0.886f
                )
                close()
                moveTo(16f, 18.001f)
                curveToRelative(0f, -4.411f, 3.589f, -8f, 8f, -8f)
                reflectiveCurveToRelative(8f, 3.589f, 8f, 8f)
                reflectiveCurveToRelative(-3.589f, 8f, -8f, 8f)
                reflectiveCurveToRelative(-8f, -3.589f, -8f, -8f)
                close()
                moveTo(35f, 28.001f)
                curveToRelative(0.68f, 0f, 1.35f, 0.099f, 2f, 0.295f)
                verticalLineToRelative(9.588f)
                curveToRelative(-4.793f, 2.767f, -7.497f, 8.038f, -6.924f, 13.601f)
                curveToRelative(0.089f, 0.864f, 0.264f, 1.704f, 0.506f, 2.517f)
                lineTo(15f, 54.002f)
                verticalLineToRelative(-11f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, -2f, 0f)
                verticalLineToRelative(11f)
                lineTo(8f, 54.002f)
                verticalLineToRelative(-19f)
                curveToRelative(0f, -3.86f, 3.141f, -7f, 7f, -7f)
                horizontalLineToRelative(20f)
                close()
            }
            path(fill = SolidColor(Color(0xFF003564))) {
                moveTo(34.219f, 52.102f)
                curveToRelative(0.834f, 4.002f, 4.109f, 7.125f, 8.152f, 7.77f)
                curveToRelative(0.536f, 0.086f, 1.073f, 0.128f, 1.606f, 0.128f)
                curveToRelative(2.378f, 0f, 4.685f, -0.835f, 6.506f, -2.39f)
                arcToRelative(
                    9.991f,
                    9.991f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -1.871f,
                    -16.475f
                )
                lineTo(47f, 40.294f)
                lineTo(47f, 7.001f)
                curveToRelative(0f, -1.654f, -1.346f, -3f, -3f, -3f)
                reflectiveCurveToRelative(-3f, 1.346f, -3f, 3f)
                verticalLineToRelative(33.293f)
                lineToRelative(-1.612f, 0.841f)
                curveToRelative(-4.023f, 2.099f, -6.1f, 6.507f, -5.169f, 10.967f)
                close()
                moveTo(40.313f, 42.908f)
                lineTo(42.462f, 41.787f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 43f, 40.9f)
                lineTo(43f, 7.001f)
                arcToRelative(
                    1.001f,
                    1.001f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    2f,
                    0f
                )
                lineTo(45f, 40.9f)
                curveToRelative(0f, 0.373f, 0.207f, 0.714f, 0.537f, 0.887f)
                lineToRelative(2.149f, 1.121f)
                arcTo(
                    7.983f,
                    7.983f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    52f,
                    50.001f
                )
                arcToRelative(
                    7.993f,
                    7.993f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -2.815f,
                    6.087f
                )
                arcToRelative(
                    8.05f,
                    8.05f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -6.5f,
                    1.809f
                )
                curveToRelative(-3.227f, -0.516f, -5.844f, -3.008f, -6.51f, -6.203f)
                curveToRelative(-0.744f, -3.575f, 0.919f, -7.105f, 4.138f, -8.786f)
                close()
            }
        }.build()

        return _BodyTemperatureIcon!!
    }

@Suppress("ObjectPropertyName")
private var _BodyTemperatureIcon: ImageVector? = null
