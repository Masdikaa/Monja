package com.masdika.monja.ui.history

import com.masdika.monja.data.model.MedicalAlert

data class HistoryScreenState(
    val macAddress: String = "",
    val alerts: List<MedicalAlert?> = emptyList(),
    val historyLoading: Boolean = true
)