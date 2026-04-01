package com.masdika.monja.ui.dashboard.bottomsheet

import com.masdika.monja.ui.theme.AestheticMaroon
import com.masdika.monja.ui.theme.DeepMarine
import com.masdika.monja.ui.theme.DeepPaprika
import com.masdika.monja.ui.theme.ElectricRed
import com.masdika.monja.ui.theme.EnglishBlue
import com.masdika.monja.ui.theme.GuardsmanRed
import com.masdika.monja.ui.theme.LightOrchid
import com.masdika.monja.ui.theme.NatureBlue
import com.masdika.monja.ui.theme.PurpleGlow
import com.masdika.monja.ui.theme.SimpleMaroon
import com.masdika.monja.ui.theme.TuttiFruitti
import com.masdika.monja.ui.theme.UpliftingPurple

object VitalColors {
    val TemperatureGradient = arrayOf(
        0.0f to SimpleMaroon,
        0.6f to GuardsmanRed,
        1f to ElectricRed
    )
    val HeartrateGradient = arrayOf(
        0.0f to AestheticMaroon,
        0.6f to DeepPaprika,
        1f to TuttiFruitti
    )
    val OxygenSaturationGradient = arrayOf(
        0.0f to EnglishBlue,
        0.6f to DeepMarine,
        1f to NatureBlue
    )
    val StatusGradient = arrayOf(
        0.0f to UpliftingPurple,
        0.6f to PurpleGlow,
        1f to LightOrchid
    )
}