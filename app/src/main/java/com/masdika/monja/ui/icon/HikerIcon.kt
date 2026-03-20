package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
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
            defaultWidth = 500.dp,
            defaultHeight = 500.dp,
            viewportWidth = 500f,
            viewportHeight = 500f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(247.83f, 136.72f)
                arcToRelative(
                    21.72f,
                    21.72f,
                    135f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    -21.72f,
                    21.72f
                )
                arcToRelative(
                    21.75f,
                    21.75f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    21.72f,
                    -21.72f
                )
                close()
                moveTo(177.15f, 242.74f)
                arcToRelative(
                    17.04f,
                    17.04f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    6.22f,
                    1.15f
                )
                arcToRelative(
                    27.24f,
                    27.24f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    17.39f,
                    -7.54f
                )
                lineToRelative(0.38f, -0.35f)
                arcToRelative(
                    112.67f,
                    112.67f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    16.02f,
                    -47.55f
                )
                curveToRelative(-1.79f, -8.2f, -6.17f, -14.14f, -12.39f, -16.49f)
                reflectiveCurveToRelative(-13.85f, -1.04f, -20.95f, 4.17f)
                arcToRelative(
                    49.93f,
                    49.93f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -16.28f,
                    22.15f
                )
                arcToRelative(
                    49.97f,
                    49.97f,
                    135f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -3.09f,
                    27.19f
                )
                curveToRelative(1.79f, 8.65f, 6.24f, 14.77f, 12.68f, 17.29f)
                close()
                moveTo(363.1f, 360.93f)
                arcToRelative(
                    2.36f,
                    2.36f,
                    81f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -2.36f,
                    2.36f
                )
                lineTo(139.26f, 363.28f)
                arcToRelative(
                    2.36f,
                    2.36f,
                    66.79f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0f,
                    -4.71f
                )
                lineToRelative(37.39f, 0f)
                arcToRelative(
                    11.95f,
                    11.95f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -0.87f,
                    -1.51f
                )
                arcToRelative(
                    10.56f,
                    10.56f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0f,
                    -8.46f
                )
                lineTo(206.95f, 274.74f)
                lineToRelative(0.19f, 0.16f)
                lineToRelative(21.82f, 18.64f)
                lineToRelative(-34.26f, 64.13f)
                curveToRelative(-0.16f, 0.31f, -0.35f, 0.61f, -0.54f, 0.9f)
                lineToRelative(52.57f, 0f)
                arcToRelative(
                    11.26f,
                    11.26f,
                    45f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -2.36f,
                    -6.79f
                )
                arcToRelative(
                    4.92f,
                    4.92f,
                    135f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0f,
                    -0.59f
                )
                lineToRelative(1.84f, -45.83f)
                arcToRelative(
                    6.43f,
                    6.43f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -2.36f,
                    -5.21f
                )
                lineTo(210.2f, 271.32f)
                arcToRelative(
                    12.39f,
                    12.39f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -4.29f,
                    -7.94f
                )
                curveTo(204.41f, 251.2f, 202.88f, 219.65f, 221.73f, 190.15f)
                arcToRelative(
                    12.51f,
                    12.51f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    15.9f,
                    -4.57f
                )
                lineTo(294.01f, 212.3f)
                arcToRelative(
                    2.85f,
                    2.85f,
                    45f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    2.36f,
                    0f
                )
                lineToRelative(16.89f, -7.07f)
                lineTo(313.26f, 168.69f)
                arcToRelative(
                    2.36f,
                    2.36f,
                    80.23f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    4.71f,
                    0f
                )
                lineToRelative(0f, 34.73f)
                lineToRelative(3.06f, -1.3f)
                arcToRelative(
                    8.46f,
                    8.46f,
                    45f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    7.61f,
                    15.08f
                )
                lineToRelative(-10.67f, 6.43f)
                lineToRelative(0f, 134.94f)
                lineTo(360.74f, 358.57f)
                arcToRelative(
                    2.36f,
                    2.36f,
                    81f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    2.36f,
                    2.36f
                )
                close()
                moveTo(313.19f, 226.44f)
                lineToRelative(-15.34f, 9.21f)
                arcToRelative(
                    7.49f,
                    7.49f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -6.29f,
                    0.68f
                )
                lineToRelative(-38.38f, -13.03f)
                arcToRelative(
                    2.47f,
                    2.47f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -2f,
                    0.21f
                )
                arcToRelative(
                    2.36f,
                    2.36f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    -1.18f,
                    1.6f
                )
                lineToRelative(-5.73f, 27.99f)
                arcToRelative(
                    2.36f,
                    2.36f,
                    83.3f,
                    isMoreThanHalf = false,
                    isPositiveArc = false,
                    0.52f,
                    2.05f
                )
                lineToRelative(21.77f, 25.92f)
                arcToRelative(
                    22.29f,
                    22.29f,
                    135f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    5.09f,
                    16.16f
                )
                lineToRelative(-4.71f, 55.42f)
                arcToRelative(
                    11.33f,
                    11.33f,
                    135f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    -2.17f,
                    5.8f
                )
                lineToRelative(48.42f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(250f, 0.36f)
                arcToRelative(
                    24.96f,
                    24.96f,
                    59.21f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    24.96f,
                    24.96f
                )
                lineToRelative(0f, 26.51f)
                arcTo(
                    199.81f,
                    199.81f,
                    135f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    448.16f,
                    225.04f
                )
                lineTo(474.67f, 225.04f)
                arcToRelative(
                    24.96f,
                    24.96f,
                    68.22f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    0f,
                    49.93f
                )
                lineToRelative(-26.51f, 0f)
                arcTo(
                    199.81f,
                    199.81f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    274.96f,
                    448.16f
                )
                lineTo(274.96f, 474.67f)
                arcToRelative(
                    24.96f,
                    24.96f,
                    111.78f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    -49.93f,
                    0f
                )
                lineToRelative(0f, -26.51f)
                arcTo(
                    199.81f,
                    199.81f,
                    45f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    51.84f,
                    274.96f
                )
                lineTo(25.33f, 274.96f)
                arcToRelative(
                    24.96f,
                    24.96f,
                    120.79f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    0f,
                    -49.93f
                )
                lineToRelative(26.51f, 0f)
                arcTo(
                    199.81f,
                    199.81f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    225.04f,
                    51.84f
                )
                lineTo(225.04f, 25.33f)
                arcToRelative(
                    24.96f,
                    24.96f,
                    59.21f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    24.96f,
                    -24.96f
                )
                close()
                moveTo(250f, 100.22f)
                arcToRelative(
                    149.78f,
                    149.78f,
                    0f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    0f,
                    299.56f
                )
                arcToRelative(
                    149.78f,
                    149.78f,
                    0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    0f,
                    -299.56f
                )
                close()
            }
        }.build()

        return _HikerIcon!!
    }

@Suppress("ObjectPropertyName")
private var _HikerIcon: ImageVector? = null