package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StatusSpO2Icon: ImageVector
    get() {
        if (_StatusSpO2Icon != null) {
            return _StatusSpO2Icon!!
        }
        _StatusSpO2Icon = ImageVector.Builder(
            name = "StatusSpO2Icon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 64f,
            viewportHeight = 64f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(18f, 19f)
                horizontalLineToRelative(-2f)
                curveToRelative(-0.552f, 0f, -1f, 0.449f, -1f, 1f)
                verticalLineToRelative(4f)
                curveToRelative(0f, 0.551f, 0.448f, 1f, 1f, 1f)
                horizontalLineToRelative(2f)
                curveToRelative(0.552f, 0f, 1f, -0.449f, 1f, -1f)
                verticalLineToRelative(-4f)
                curveToRelative(0f, -0.551f, -0.448f, -1f, -1f, -1f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveToRelative(42.333f, 11.255f)
                lineToRelative(0.667f, -0.597f)
                lineToRelative(0.667f, 0.597f)
                curveToRelative(0.227f, 0.203f, 1.924f, 1.737f, 4.222f, 4.152f)
                arcToRelative(
                    9.92f,
                    9.92f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    1.881f,
                    -0.87f
                )
                arcTo(13.983f, 13.983f, 0f, isMoreThanHalf = false, isPositiveArc = false, 36f, 3f)
                curveToRelative(-3.4f, 0f, -6.68f, 1.234f, -9.232f, 3.475f)
                lineToRelative(-0.785f, 0.689f)
                lineToRelative(-0.655f, -0.814f)
                curveTo(25.219f, 6.217f, 22.521f, 3f, 17f, 3f)
                curveTo(10.383f, 3f, 5f, 8.383f, 5f, 15f)
                curveToRelative(0f, 2.093f, 0.546f, 4.152f, 1.579f, 5.955f)
                lineToRelative(0.593f, 1.034f)
                lineToRelative(-1.121f, 0.404f)
                curveTo(5.932f, 22.438f, 3f, 23.572f, 3f, 27f)
                curveToRelative(0f, 2.757f, 2.243f, 5f, 5f, 5f)
                horizontalLineToRelative(17.968f)
                curveToRelative(5.271f, -10.725f, 15.775f, -20.217f, 16.365f, -20.745f)
                close()
                moveTo(21f, 24f)
                curveToRelative(0f, 1.654f, -1.346f, 3f, -3f, 3f)
                horizontalLineToRelative(-2f)
                curveToRelative(-1.654f, 0f, -3f, -1.346f, -3f, -3f)
                verticalLineToRelative(-4f)
                curveToRelative(0f, -1.654f, 1.346f, -3f, 3f, -3f)
                horizontalLineToRelative(2f)
                curveToRelative(1.654f, 0f, 3f, 1.346f, 3f, 3f)
                close()
                moveTo(23f, 19f)
                verticalLineToRelative(-2f)
                curveToRelative(0f, -1.103f, 0.897f, -2f, 2f, -2f)
                horizontalLineToRelative(2f)
                verticalLineToRelative(-2f)
                horizontalLineToRelative(-4f)
                verticalLineToRelative(-2f)
                horizontalLineToRelative(4f)
                curveToRelative(1.103f, 0f, 2f, 0.897f, 2f, 2f)
                verticalLineToRelative(2f)
                curveToRelative(0f, 1.103f, -0.897f, 2f, -2f, 2f)
                horizontalLineToRelative(-2f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(4f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(-4f)
                curveToRelative(-1.103f, 0f, -2f, -0.897f, -2f, -2f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(43f, 13.365f)
                curveTo(39.667f, 16.518f, 25f, 31.072f, 25f, 43f)
                curveToRelative(0f, 9.925f, 8.075f, 18f, 18f, 18f)
                reflectiveCurveToRelative(18f, -8.075f, 18f, -18f)
                curveToRelative(0f, -11.936f, -14.667f, -26.484f, -18f, -29.635f)
                close()
                moveTo(33f, 31f)
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    0.001f,
                    3.999f
                )
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 33f, 31f)
                close()
                moveTo(34f, 50f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -12f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 12f)
                close()
                moveTo(41f, 58f)
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    -0.001f,
                    -3.999f
                )
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 41f, 58f)
                close()
                moveTo(54f, 36f)
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    0.001f,
                    3.999f
                )
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 54f, 36f)
                close()
                moveTo(58f, 47f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, -14f, 0f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14f, 0f)
                close()
                moveTo(50f, 30f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = true, -12f, 0f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 0f)
                close()
                moveTo(43f, 24f)
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    -0.001f,
                    -3.999f
                )
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 43f, 24f)
                close()
                moveTo(58.737f, 29.563f)
                arcTo(7.926f, 7.926f, 0f, isMoreThanHalf = false, isPositiveArc = false, 61f, 24f)
                curveToRelative(0f, -4.411f, -3.589f, -8f, -8f, -8f)
                arcToRelative(
                    7.945f,
                    7.945f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -3.705f,
                    0.915f
                )
                curveToRelative(3.017f, 3.309f, 6.667f, 7.783f, 9.442f, 12.648f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(51f, 47f)
                moveToRelative(-3f, 0f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = true, 6f, 0f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6f, 0f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(34f, 44f)
                moveToRelative(-2f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(44f, 30f)
                moveToRelative(-2f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
            }
        }.build()

        return _StatusSpO2Icon!!
    }

@Suppress("ObjectPropertyName")
private var _StatusSpO2Icon: ImageVector? = null