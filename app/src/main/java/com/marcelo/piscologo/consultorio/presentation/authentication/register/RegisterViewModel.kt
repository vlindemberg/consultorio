package com.marcelo.piscologo.consultorio.presentation.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {

    private val _register = MutableStateFlow<RegisterState<*>?>(null)
    val register: StateFlow<RegisterState<*>?> = _register

    fun register(
        email: String,
        password: String,
        user: User
    ) = viewModelScope.launch {
        _register.value = RegisterState.Loading
        _register.value = repository.registerUser(
            email = email,
            password = password,
            user = user
        )
    }
}