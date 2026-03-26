package com.masdika.monja.ui.history

sealed interface HistoryScreenEvent {
    data class ShowSnackbar(val message: String) : HistoryScreenEvent
}