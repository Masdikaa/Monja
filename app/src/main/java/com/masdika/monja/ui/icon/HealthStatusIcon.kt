package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
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
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(fill = SolidColor(Color(0xFF707487))) {
                moveTo(359.21f, 57.5f)
                horizontalLineToRelative(-73.18f)
                verticalLineToRelative(12.03f)
                curveToRelative(0f, 6.84f, -5.6f, 12.44f, -12.44f, 12.44f)
                horizontalLineTo(124.89f)
                curveToRelative(-6.84f, 0f, -12.44f, -5.6f, -12.44f, -12.44f)
                verticalLineToRelative(-12.03f)
                horizontalLineTo(39.27f)
                curveToRelative(-15.43f, 0f, -27.98f, 12.55f, -27.98f, 27.98f)
                verticalLineTo(484.02f)
                curveToRelative(0f, 15.43f, 12.55f, 27.98f, 27.98f, 27.98f)
                horizontalLineToRelative(319.94f)
                curveToRelative(15.43f, 0f, 27.98f, -12.55f, 27.98f, -27.98f)
                verticalLineTo(85.48f)
                curveToRelative(0f, -15.43f, -12.55f, -27.98f, -27.98f, -27.98f)
                close()
            }
            path(fill = SolidColor(Color(0xFF5B5D6E))) {
                moveTo(31.9f, 484.02f)
                verticalLineTo(85.48f)
                curveToRelative(0f, -15.43f, 12.55f, -27.98f, 27.98f, -27.98f)
                horizontalLineTo(39.27f)
                curveToRelative(-15.43f, 0f, -27.98f, 12.55f, -27.98f, 27.98f)
                verticalLineTo(484.02f)
                curveToRelative(0f, 15.43f, 12.55f, 27.98f, 27.98f, 27.98f)
                horizontalLineToRelative(20.6f)
                curveTo(44.45f, 512f, 31.9f, 499.45f, 31.9f, 484.02f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE4EAF8))) {
                moveTo(356.1f, 390.67f)
                verticalLineTo(117.04f)
                curveToRelative(0f, -3.23f, -2.78f, -5.85f, -6.2f, -5.85f)
                horizontalLineTo(48.58f)
                curveToRelative(-3.42f, 0f, -6.2f, 2.62f, -6.2f, 5.85f)
                verticalLineToRelative(358.02f)
                curveToRelative(0f, 3.23f, 2.78f, 5.85f, 6.2f, 5.85f)
                horizontalLineToRelative(217.28f)
                lineToRelative(90.24f, -90.24f)
                close()
            }
            path(fill = SolidColor(Color(0xFFD8DCE5))) {
                moveTo(62.99f, 475.06f)
                verticalLineTo(117.04f)
                curveToRelative(0f, -3.23f, 2.78f, -5.85f, 6.2f, -5.85f)
                horizontalLineTo(48.58f)
                curveToRelative(-3.42f, 0f, -6.2f, 2.62f, -6.2f, 5.85f)
                verticalLineToRelative(358.02f)
                curveToRelative(0f, 3.23f, 2.78f, 5.85f, 6.2f, 5.85f)
                horizontalLineToRelative(20.6f)
                curveToRelative(-3.42f, 0f, -6.2f, -2.62f, -6.2f, -5.85f)
                close()
            }
            path(fill = SolidColor(Color(0xFF00C3FF))) {
                moveTo(253.72f, 148.13f)
                horizontalLineToRelative(-108.95f)
                curveToRelative(-9.4f, 0f, -17.05f, -7.65f, -17.05f, -17.05f)
                verticalLineTo(77.84f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    15.45f,
                    0f
                )
                verticalLineToRelative(53.24f)
                curveToRelative(0f, 0.88f, 0.71f, 1.6f, 1.6f, 1.6f)
                horizontalLineToRelative(108.95f)
                curveToRelative(0.88f, 0f, 1.6f, -0.72f, 1.6f, -1.6f)
                verticalLineTo(77.84f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    15.45f,
                    0f
                )
                verticalLineToRelative(53.24f)
                curveToRelative(-0f, 9.4f, -7.65f, 17.05f, -17.05f, 17.05f)
                close()
            }
            path(fill = SolidColor(Color(0xFFD8DCE5))) {
                moveTo(223.86f, 32.72f)
                verticalLineToRelative(-20.29f)
                curveTo(223.86f, 5.6f, 218.27f, 0f, 211.43f, 0f)
                horizontalLineToRelative(-24.37f)
                curveToRelative(-6.84f, 0f, -12.44f, 5.6f, -12.44f, 12.44f)
                verticalLineToRelative(20.29f)
                lineToRelative(49.24f, 0f)
                close()
            }
            path(fill = SolidColor(Color(0xFFC3C8D1))) {
                moveTo(208.04f, 0f)
                horizontalLineToRelative(-20.99f)
                curveToRelative(-6.84f, 0f, -12.44f, 5.6f, -12.44f, 12.44f)
                verticalLineToRelative(20.29f)
                horizontalLineToRelative(20.99f)
                verticalLineTo(12.44f)
                curveTo(195.61f, 5.6f, 201.21f, 0f, 208.04f, 0f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE4EAF8))) {
                moveTo(273.67f, 32.21f)
                horizontalLineToRelative(-148.85f)
                curveToRelative(-7.1f, 0f, -12.88f, 5.78f, -12.88f, 12.88f)
                verticalLineTo(69.6f)
                curveToRelative(0f, 7.1f, 5.78f, 12.88f, 12.88f, 12.88f)
                horizontalLineToRelative(148.85f)
                curveToRelative(7.1f, 0f, 12.88f, -5.78f, 12.88f, -12.88f)
                verticalLineTo(45.09f)
                curveToRelative(0f, -7.1f, -5.78f, -12.88f, -12.88f, -12.88f)
                close()
            }
            path(fill = SolidColor(Color(0xFFD8DCE5))) {
                moveTo(273.67f, 61.87f)
                horizontalLineToRelative(-148.85f)
                curveToRelative(-7.1f, 0f, -12.88f, -5.78f, -12.88f, -12.88f)
                verticalLineTo(69.6f)
                curveToRelative(0f, 7.1f, 5.78f, 12.88f, 12.88f, 12.88f)
                horizontalLineToRelative(148.85f)
                curveToRelative(7.1f, 0f, 12.88f, -5.78f, 12.88f, -12.88f)
                verticalLineTo(49f)
                curveToRelative(0f, 7.1f, -5.78f, 12.88f, -12.88f, 12.88f)
                close()
                moveTo(356.1f, 390.67f)
                horizontalLineToRelative(-84.04f)
                curveToRelative(-3.42f, 0f, -6.2f, 2.62f, -6.2f, 5.85f)
                verticalLineToRelative(84.39f)
                lineToRelative(90.24f, -90.24f)
                close()
            }
            path(fill = SolidColor(Color(0xFFC3C8D1))) {
                moveTo(286.46f, 396.52f)
                curveToRelative(0f, -3.23f, 2.78f, -5.85f, 6.2f, -5.85f)
                horizontalLineToRelative(-20.6f)
                curveToRelative(-3.42f, 0f, -6.2f, 2.62f, -6.2f, 5.85f)
                verticalLineToRelative(84.39f)
                lineToRelative(20.6f, -20.6f)
                verticalLineToRelative(-63.78f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFF6161))) {
                moveTo(453.27f, 81.96f)
                curveToRelative(-17.68f, 0.02f, -33.07f, 9.72f, -41.2f, 24.09f)
                lineToRelative(-0f, -0f)
                reflectiveCurveToRelative(-2.64f, 3.27f, -6.15f, 3.27f)
                curveToRelative(-3.51f, 0f, -6.15f, -3.26f, -6.15f, -3.26f)
                horizontalLineToRelative(-0.01f)
                curveToRelative(-8.15f, -14.34f, -23.56f, -24.02f, -41.24f, -24f)
                curveToRelative(-26.17f, 0.03f, -47.36f, 21.26f, -47.33f, 47.43f)
                curveToRelative(0.07f, 68.36f, 75.35f, 108.2f, 91.75f, 116.07f)
                curveToRelative(0.01f, 0f, 2.9f, 1.11f, 6.25f, 0f)
                curveToRelative(16.36f, -7.89f, 91.59f, -47.88f, 91.52f, -116.26f)
                curveToRelative(-0.03f, -26.17f, -21.26f, -47.36f, -47.43f, -47.33f)
                close()
            }
            path(fill = SolidColor(Color(0xFFDD4343))) {
                moveTo(331.79f, 129.49f)
                curveToRelative(-0.02f, -22.64f, 15.84f, -41.57f, 37.06f, -46.29f)
                arcToRelative(
                    47.5f,
                    47.5f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -10.34f,
                    -1.14f
                )
                curveToRelative(-26.17f, 0.03f, -47.36f, 21.26f, -47.33f, 47.43f)
                curveToRelative(0.07f, 68.36f, 75.35f, 108.2f, 91.75f, 116.07f)
                curveToRelative(0.01f, 0f, 2.9f, 1.11f, 6.25f, -0f)
                curveToRelative(1.76f, -0.85f, 4.2f, -2.07f, 7.16f, -3.66f)
                curveToRelative(-24.57f, -13.13f, -84.49f, -51.42f, -84.55f, -112.41f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFF6161))) {
                moveTo(178.4f, 355.62f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -7.48f,
                    -5.78f
                )
                lineToRelative(-25.85f, -99.09f)
                lineToRelative(-11.19f, 36.76f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -7.39f,
                    5.48f
                )
                horizontalLineTo(89.49f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0f,
                    -15.45f
                )
                horizontalLineToRelative(31.27f)
                lineToRelative(17.49f, -57.46f)
                curveToRelative(1.01f, -3.31f, 4.1f, -5.57f, 7.55f, -5.47f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.32f,
                    5.77f
                )
                lineToRelative(26.52f, 101.68f)
                lineToRelative(14.53f, -39.61f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.25f,
                    -5.07f
                )
                horizontalLineToRelative(25.92f)
                arcToRelative(
                    7.72f,
                    7.72f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.25f,
                    5.04f
                )
                lineToRelative(2.93f, 7.91f)
                lineToRelative(14.44f, -56.31f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.23f,
                    -5.8f
                )
                arcToRelative(
                    7.71f,
                    7.71f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.6f,
                    5.31f
                )
                lineToRelative(14.39f, 43.84f)
                horizontalLineToRelative(28.22f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0f,
                    15.45f
                )
                horizontalLineToRelative(-33.82f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -7.34f,
                    -5.32f
                )
                lineToRelative(-7.88f, -24.01f)
                lineToRelative(-14f, 54.63f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -14.73f,
                    0.76f
                )
                lineToRelative(-9.65f, -26.06f)
                horizontalLineToRelative(-15.14f)
                lineToRelative(-21.18f, 57.72f)
                arcToRelative(
                    7.73f,
                    7.73f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -7.25f,
                    5.07f
                )
                close()
            }
        }.build()

        return _HealthStatusIcon!!
    }

@Suppress("ObjectPropertyName")
private var _HealthStatusIcon: ImageVector? = null