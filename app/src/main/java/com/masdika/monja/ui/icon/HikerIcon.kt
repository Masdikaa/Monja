package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val HikerIcon: ImageVector
    get() {
        if (_HikerIcon != null) {
            return _HikerIcon!!
        }
        _HikerIcon = ImageVector.Builder(
            name = "HikerIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 64f,
            viewportHeight = 64f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(32f, 2f)
                arcToRelative(30f, 30f, 0f, isMoreThanHalf = true, isPositiveArc = false, 30f, 30f)
                arcTo(30.034f, 30.034f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32f, 2f)
                close()
                moveTo(32f, 60f)
                arcTo(
                    28.034f,
                    28.034f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    6.722f,
                    44.034f
                )
                arcTo(
                    5.989f,
                    5.989f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    11.918f,
                    41f
                )
                arcToRelative(
                    5.907f,
                    5.907f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    1.839f,
                    0.29f
                )
                lineToRelative(0.8f, 0.263f)
                lineToRelative(0.394f, -0.745f)
                arcToRelative(
                    9.14f,
                    9.14f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    2.573f,
                    -3.008f
                )
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    -1.2f,
                    -1.6f
                )
                arcToRelative(
                    11.2f,
                    11.2f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -2.736f,
                    2.976f
                )
                arcToRelative(
                    7.952f,
                    7.952f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -7.782f,
                    2.7f
                )
                arcToRelative(
                    27.777f,
                    27.777f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.66f,
                    -7.006f
                )
                lineToRelative(10.33f, -13.541f)
                arcToRelative(
                    8.148f,
                    8.148f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    4.671f,
                    -1.506f
                )
                arcToRelative(
                    5.72f,
                    5.72f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    6.82f,
                    -0.048f
                )
                lineToRelative(0.148f, 0.1f)
                reflectiveCurveToRelative(0.235f, 0.131f, 0.631f, 0.3f)
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    0.782f,
                    -1.841f
                )
                arcToRelative(
                    6.95f,
                    6.95f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.382f,
                    -0.176f
                )
                arcToRelative(
                    7.739f,
                    7.739f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -9.137f,
                    0.02f
                )
                arcToRelative(
                    6.485f,
                    6.485f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.8f,
                    0.876f
                )
                lineToRelative(4.81f, -6.306f)
                arcToRelative(
                    4.979f,
                    4.979f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    5.76f,
                    -1.638f
                )
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    0.711f,
                    -1.869f
                )
                arcToRelative(
                    6.96f,
                    6.96f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -8.059f,
                    2.293f
                )
                lineTo(4.006f, 31.756f)
                arcTo(28f, 28f, 0f, isMoreThanHalf = false, isPositiveArc = true, 60f, 32f)
                curveToRelative(0f, 0.6f, -0.025f, 1.192f, -0.063f, 1.781f)
                lineToRelative(-7.348f, -9.849f)
                arcToRelative(
                    6.919f,
                    6.919f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -4.835f,
                    -2.771f
                )
                arcToRelative(
                    7.013f,
                    7.013f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -5.328f,
                    1.639f
                )
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    1.3f,
                    1.516f
                )
                arcToRelative(
                    5.021f,
                    5.021f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    3.805f,
                    -1.167f
                )
                arcToRelative(
                    4.939f,
                    4.939f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    3.451f,
                    1.978f
                )
                lineToRelative(3.019f, 4.047f)
                lineToRelative(-0.085f, 0.064f)
                arcToRelative(
                    3.835f,
                    3.835f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -3.611f,
                    0.379f
                )
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -0.582f,
                    1.914f
                )
                arcToRelative(
                    7.82f,
                    7.82f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    2.262f,
                    0.363f
                )
                arcToRelative(
                    4.821f,
                    4.821f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    3.21f,
                    -1.119f
                )
                lineToRelative(4.4f, 5.9f)
                arcToRelative(
                    27.73f,
                    27.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.131f,
                    4.432f
                )
                arcToRelative(
                    10.967f,
                    10.967f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -10.674f,
                    -3.9f
                )
                arcToRelative(
                    1f,
                    1f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = false,
                    0.385f,
                    1.962f
                )
                arcToRelative(
                    8.983f,
                    8.983f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    9.425f,
                    4.162f
                )
                arcTo(28.034f, 28.034f, 0f, isMoreThanHalf = false, isPositiveArc = true, 32f, 60f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(30f, 20f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = false, 11.91f, -1f)
                lineTo(47f, 19f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2f)
                horizontalLineToRelative(-4.08f)
                arcTo(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = false, 29f, 18f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(1.089f)
                arcTo(6.055f, 6.055f, 0f, isMoreThanHalf = false, isPositiveArc = false, 30f, 20f)
                close()
                moveTo(40f, 20f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7.858f, -1f)
                horizontalLineToRelative(7.717f)
                arcTo(3.983f, 3.983f, 0f, isMoreThanHalf = false, isPositiveArc = true, 40f, 20f)
                close()
                moveTo(36f, 13f)
                arcToRelative(
                    5.008f,
                    5.008f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    4.9f,
                    4f
                )
                horizontalLineToRelative(-9.8f)
                arcToRelative(
                    5.008f,
                    5.008f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    4.9f,
                    -4f
                )
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(47.028f, 29.066f)
                arcToRelative(
                    3.606f,
                    3.606f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -4.281f,
                    -2.166f
                )
                lineTo(36.3f, 28.524f)
                arcToRelative(
                    5.994f,
                    5.994f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -6.928f,
                    -3.352f
                )
                arcToRelative(
                    6.129f,
                    6.129f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -1f,
                    0.357f
                )
                arcToRelative(
                    4.031f,
                    4.031f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -0.1f,
                    -1.16f
                )
                arcToRelative(
                    4f,
                    4f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -7.313f,
                    -1.078f
                )
                lineTo(17.365f, 29.3f)
                arcToRelative(
                    4f,
                    4f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    3.426f,
                    6.053f
                )
                curveToRelative(0.125f, 0f, 0.249f, -0.012f, 0.373f, -0.024f)
                arcToRelative(
                    9.779f,
                    9.779f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -0.2f,
                    0.514f
                )
                arcToRelative(
                    17.515f,
                    17.515f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -0.3f,
                    7.124f
                )
                arcToRelative(
                    3f,
                    3f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.015f,
                    2.175f
                )
                lineTo(16.1f, 48.279f)
                arcToRelative(
                    2.863f,
                    2.863f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -0.131f,
                    4.2f
                )
                arcToRelative(
                    3.177f,
                    3.177f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    4.288f,
                    0.134f
                )
                lineToRelative(4.726f, -4.168f)
                arcToRelative(
                    5f,
                    5f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    1.693f,
                    -3.75f
                )
                verticalLineToRelative(-2.207f)
                lineToRelative(0.407f, 0.243f)
                lineToRelative(2.9f, 3.258f)
                arcToRelative(
                    3f,
                    3f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0.737f,
                    2.334f
                )
                lineToRelative(-0.475f, 4.149f)
                arcToRelative(
                    2.935f,
                    2.935f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    2.678f,
                    3.264f
                )
                curveToRelative(0.084f, 0.007f, 0.167f, 0.01f, 0.25f, 0.01f)
                arcToRelative(
                    3.089f,
                    3.089f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    3.048f,
                    -2.715f
                )
                lineToRelative(0.664f, -5.8f)
                arcToRelative(
                    5f,
                    5f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -1.231f,
                    -3.89f
                )
                lineToRelative(-3.115f, -3.5f)
                lineToRelative(2.215f, -3.7f)
                lineTo(43f, 34.056f)
                lineTo(43f, 54f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 0f)
                lineTo(45f, 33.506f)
                arcToRelative(
                    3.464f,
                    3.464f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    2.028f,
                    -4.44f
                )
                close()
                moveTo(19.771f, 33.066f)
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.689f,
                    -2.743f
                )
                lineToRelative(3.593f, -6.008f)
                arcToRelative(
                    1.984f,
                    1.984f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    1.228f,
                    -0.912f
                )
                arcToRelative(
                    2.015f,
                    2.015f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0.492f,
                    -0.061f
                )
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    1.713f,
                    3.028f
                )
                lineToRelative(-3.594f, 6.006f)
                arcToRelative(
                    2f,
                    2f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -2.743f,
                    0.69f
                )
                close()
                moveTo(45.063f, 30.966f)
                arcToRelative(
                    1.5f,
                    1.5f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.959f,
                    0.75f
                )
                lineToRelative(-10.647f, 2.677f)
                lineToRelative(-3.394f, 5.673f)
                lineToRelative(4.091f, 4.6f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 34.893f, 47f)
                lineToRelative(-0.664f, 5.8f)
                arcToRelative(
                    1.083f,
                    1.083f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.147f,
                    0.939f
                )
                arcToRelative(
                    0.932f,
                    0.932f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.855f,
                    -1.044f
                )
                lineToRelative(0.473f, -4.145f)
                arcToRelative(
                    5.006f,
                    5.006f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -1.231f,
                    -3.89f
                )
                lineToRelative(-3f, -3.372f)
                lineToRelative(-0.773f, -0.517f)
                arcToRelative(
                    1.979f,
                    1.979f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -2.014f,
                    -0.023f
                )
                arcToRelative(
                    1.978f,
                    1.978f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -1.013f,
                    1.74f
                )
                lineTo(24.669f, 44.7f)
                arcToRelative(
                    3f,
                    3f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.016f,
                    2.25f
                )
                lineToRelative(-4.726f, 4.168f)
                arcToRelative(
                    1.162f,
                    1.162f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -1.551f,
                    -0.049f
                )
                arcToRelative(
                    0.863f,
                    0.863f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0.04f,
                    -1.285f
                )
                lineToRelative(3.56f, -3.14f)
                arcToRelative(
                    5f,
                    5f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    1.693f,
                    -3.749f
                )
                lineToRelative(-0.015f, -0.171f)
                arcToRelative(
                    15.888f,
                    15.888f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0.213f,
                    -6.264f
                )
                curveToRelative(0.527f, -1.637f, 3.417f, -5.936f, 4.526f, -7.517f)
                arcToRelative(
                    3.995f,
                    3.995f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.295f,
                    1.033f
                )
                lineToRelative(0.253f, 0.954f)
                lineToRelative(8.289f, -2.084f)
                arcToRelative(
                    1.6f,
                    1.6f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    1.912f,
                    0.9f
                )
                arcToRelative(
                    1.486f,
                    1.486f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.079f,
                    1.22f
                )
                close()
            }
        }.build()

        return _HikerIcon!!
    }

@Suppress("ObjectPropertyName")
private var _HikerIcon: ImageVector? = null
