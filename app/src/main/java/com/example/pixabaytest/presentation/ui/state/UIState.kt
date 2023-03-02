package com.example.pixabaytest.presentation.ui.state

sealed class UIState<T> {
    class Empty<T> : UIState<T>()
    class Loading<T> : UIState<T>()
    class Success<T>(val data: T) : UIState<T>()
    class Error<T>(val message: String) : UIState<T>()
}