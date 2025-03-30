package com.mayag.omdbbrowser.presentation.state

sealed class UiState<out T> {
    data object None : UiState<Nothing>()       // Initial or idle state
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

