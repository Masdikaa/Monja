package com.masdika.monja.ui.history

import com.masdika.monja.data.model.MedicalAlert
import com.masdika.monja.data.utils.Result

data class HistoryScreenState(
    val macAddress: String = "",
    val statusState: Result<List<MedicalAlert>> = Result.Loading,
    val showDeleteDialog: Boolean = false
)