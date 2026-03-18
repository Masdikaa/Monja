package com.masdika.monja.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SpO2Icon: ImageVector
    get() {
        if (_SpO2Icon != null) {
            return _SpO2Icon!!
        }
        _SpO2Icon = ImageVector.Builder(
            name = "SpO2Icon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 64f,
            viewportHeight = 64f
        ).apply {
            path(fill = SolidColor(Color(0xFFC80A50))) {
                moveTo(22.891f, 22.706f)
                curveToRelative(-3.061f, 2.069f, -11.486f, 8.618f, -12.735f, 18.752f)
                curveToRelative(-1.047f, 8.494f, 5.119f, 16.441f, 13.677f, 16.541f)
                curveTo(31.641f, 58.09f, 38f, 51.788f, 38f, 44f)
                curveToRelative(0f, -11.566f, -9.555f, -19.037f, -12.877f, -21.285f)
                arcToRelative(1.988f, 1.988f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.232f, -0.009f)
                close()
            }
            path(fill = SolidColor(Color(0xFFA00028))) {
                moveTo(32.21f, 32.76f)
                curveToRelative(-0.14f, 0.72f, -0.21f, 1.47f, -0.21f, 2.24f)
                curveToRelative(0f, 4.418f, 2.384f, 8.276f, 5.935f, 10.357f)
                curveToRelative(0.043f, -0.446f, 0.065f, -0.899f, 0.065f, -1.357f)
                curveToRelative(0f, -3.331f, -0.793f, -6.322f, -2f, -8.947f)
                arcToRelative(11.986f, 11.986f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.79f, -2.293f)
                close()
            }
            path(fill = SolidColor(Color(0xFFA00028))) {
                moveTo(24f, 43.999f)
                moveToRelative(-12f, 0f)
                arcToRelative(12f, 12f, 0f, isMoreThanHalf = true, isPositiveArc = true, 24f, 0f)
                arcToRelative(12f, 12f, 0f, isMoreThanHalf = true, isPositiveArc = true, -24f, 0f)
            }
            path(fill = SolidColor(Color(0xFF82000A))) {
                moveTo(35.999f, 43.946f)
                arcTo(11.97f, 11.97f, 0f, isMoreThanHalf = false, isPositiveArc = false, 32f, 35.056f)
                arcToRelative(11.974f, 11.974f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.999f, 8.89f)
                close()
            }
            path(fill = SolidColor(Color(0xFFC80A50))) {
                moveTo(30.432f, 5.748f)
                curveToRelative(-1.652f, 2.066f, -4.073f, 5.667f, -4.406f, 9.377f)
                curveToRelative(-0.298f, 3.315f, 1.951f, 6.429f, 5.258f, 6.805f)
                arcToRelative(6.002f, 6.002f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.703f, -5.96f)
                curveToRelative(0f, -4.003f, -2.67f, -8.006f, -4.448f, -10.227f)
                arcToRelative(1.991f, 1.991f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.107f, 0.005f)
                close()
            }
            path(fill = SolidColor(Color(0xFFA00028))) {
                moveTo(19.988f, 26.971f)
                curveToRelative(0f, -0.635f, -0.068f, -1.271f, -0.188f, -1.899f)
                curveToRelative(-2.146f, 1.838f, -4.712f, 4.475f, -6.68f, 7.838f)
                lineToRelative(0.165f, 0.021f)
                arcToRelative(6.002f, 6.002f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.703f, -5.96f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFAB400))) {
                moveTo(43f, 44f)
                horizontalLineToRelative(2f)
                verticalLineToRelative(4f)
                horizontalLineToRelative(-2f)
                close()
            }
            path(fill = SolidColor(Color(0xFFC80A50))) {
                moveTo(43f, 48f)
                horizontalLineToRelative(2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                verticalLineToRelative(10f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 1f)
                horizontalLineToRelative(-2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, -1f)
                verticalLineTo(49f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, -1f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFAB400))) {
                moveTo(44f, 34f)
                moveToRelative(-12f, 0f)
                arcToRelative(12f, 12f, 0f, isMoreThanHalf = true, isPositiveArc = true, 24f, 0f)
                arcToRelative(12f, 12f, 0f, isMoreThanHalf = true, isPositiveArc = true, -24f, 0f)
            }
            path(fill = SolidColor(Color(0xFF0A5078))) {
                moveTo(44f, 34f)
                moveToRelative(-10f, 0f)
                arcToRelative(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, 20f, 0f)
                arcToRelative(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, -20f, 0f)
            }
            path(fill = SolidColor(Color(0xFFF0F0F0))) {
                moveTo(20f, 49f)
                curveToRelative(-2.206f, 0f, -4f, -1.794f, -4f, -4f)
                verticalLineToRelative(-4f)
                curveToRelative(0f, -2.206f, 1.794f, -4f, 4f, -4f)
                reflectiveCurveToRelative(4f, 1.794f, 4f, 4f)
                verticalLineToRelative(4f)
                curveToRelative(0f, 2.206f, -1.794f, 4f, -4f, 4f)
                close()
                moveTo(20f, 39f)
                curveToRelative(-1.103f, 0f, -2f, 0.897f, -2f, 2f)
                verticalLineToRelative(4f)
                curveToRelative(0f, 1.103f, 0.897f, 2f, 2f, 2f)
                reflectiveCurveToRelative(2f, -0.897f, 2f, -2f)
                verticalLineToRelative(-4f)
                curveToRelative(0f, -1.103f, -0.897f, -2f, -2f, -2f)
                close()
                moveTo(31f, 51f)
                horizontalLineToRelative(-4f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, -1f)
                verticalLineToRelative(-0.5f)
                curveToRelative(0f, -1.93f, 1.57f, -3.5f, 3.5f, -3.5f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1f)
                lineTo(27f, 45f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -2f)
                horizontalLineToRelative(2.5f)
                curveToRelative(1.378f, 0f, 2.5f, 1.122f, 2.5f, 2.5f)
                reflectiveCurveTo(30.878f, 48f, 29.5f, 48f)
                curveToRelative(-0.652f, 0f, -1.208f, 0.418f, -1.415f, 1f)
                lineTo(31f, 49f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 2f)
                close()
            }
            path(fill = SolidColor(Color(0xFFC80A50))) {
                moveTo(12.432f, 15.748f)
                curveToRelative(-1.652f, 2.066f, -4.073f, 5.667f, -4.406f, 9.377f)
                curveToRelative(-0.298f, 3.315f, 1.951f, 6.429f, 5.258f, 6.805f)
                arcToRelative(6.002f, 6.002f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.703f, -5.96f)
                curveToRelative(0f, -4.003f, -2.67f, -8.006f, -4.448f, -10.227f)
                arcToRelative(1.991f, 1.991f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.107f, 0.005f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF0F0F0))) {
                moveTo(39.999f, 41f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.831f, -1.555f)
                lineToRelative(8f, -12f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.664f, 1.11f)
                lineToRelative(-8f, 12f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.833f, 0.445f)
                close()
                moveTo(40f, 33f)
                curveToRelative(-1.654f, 0f, -3f, -1.346f, -3f, -3f)
                reflectiveCurveToRelative(1.346f, -3f, 3f, -3f)
                reflectiveCurveToRelative(3f, 1.346f, 3f, 3f)
                reflectiveCurveToRelative(-1.346f, 3f, -3f, 3f)
                close()
                moveTo(40f, 29f)
                curveToRelative(-0.551f, 0f, -1f, 0.449f, -1f, 1f)
                reflectiveCurveToRelative(0.449f, 1f, 1f, 1f)
                reflectiveCurveToRelative(1f, -0.449f, 1f, -1f)
                reflectiveCurveToRelative(-0.449f, -1f, -1f, -1f)
                close()
                moveTo(48f, 41f)
                curveToRelative(-1.654f, 0f, -3f, -1.346f, -3f, -3f)
                reflectiveCurveToRelative(1.346f, -3f, 3f, -3f)
                reflectiveCurveToRelative(3f, 1.346f, 3f, 3f)
                reflectiveCurveToRelative(-1.346f, 3f, -3f, 3f)
                close()
                moveTo(48f, 37f)
                curveToRelative(-0.551f, 0f, -1f, 0.449f, -1f, 1f)
                reflectiveCurveToRelative(0.449f, 1f, 1f, 1f)
                reflectiveCurveToRelative(1f, -0.449f, 1f, -1f)
                reflectiveCurveToRelative(-0.449f, -1f, -1f, -1f)
                close()
            }
        }.build()

        return _SpO2Icon!!
    }

@Suppress("ObjectPropertyName")
private var _SpO2Icon: ImageVector? = null