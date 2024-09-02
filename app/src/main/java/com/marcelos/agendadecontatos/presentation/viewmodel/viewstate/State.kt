package com.marcelos.agendadecontatos.presentation.viewmodel.viewstate

sealed interface State<T> {
    class Loading<T> : State<T>
    data class Success<T>(val data: T) : State<T>
    open class Error<T>(val error: Throwable) : State<T>
}