package com.marcelo.piscologo.consultorio.presentation.authentication

sealed class AuthenticationState<out T> {
    object Loading: AuthenticationState<Nothing>()
    data class Success<out T>(val data: T): AuthenticationState<T>()
    data class Failure(val exception: Exception): AuthenticationState<Nothing>()
}