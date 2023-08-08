package com.marcelo.piscologo.consultorio.presentation.authentication.register

sealed class RegisterState<out T> {
    object Loading : RegisterState<Nothing>()
    data class Success<out T>(val data: T) : RegisterState<T>()
    data class Failure(val exception: Exception) : RegisterState<Nothing>()
}