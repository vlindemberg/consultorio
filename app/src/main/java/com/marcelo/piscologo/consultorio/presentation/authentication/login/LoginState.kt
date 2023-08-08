package com.marcelo.piscologo.consultorio.presentation.authentication.login

sealed class LoginState<out T> {
    object Loading: LoginState<Nothing>()
    data class Success<out T>(val data: T): LoginState<T>()
    data class Failure(val exception: Exception): LoginState<Nothing>()
}