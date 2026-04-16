package com.masdika.monja.ui.analytic

import com.masdika.monja.data.model.Vitals
import com.masdika.monja.data.Result

data class AnalyticScreenState(
    val vitalState: Result<List<Vitals>> = Result.Loading
)