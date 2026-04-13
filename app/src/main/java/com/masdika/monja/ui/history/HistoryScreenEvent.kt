package com.masdika.monja.ui.history

import com.masdika.monja.util.UiText

sealed interface HistoryScreenEvent {
    data class ShowSnackbar(val message: UiText) : HistoryScreenEvent
}