package com.marcelo.piscologo.consultorio.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<AuthenticationState<FirebaseUser>?>(null)
    val loginFlow: StateFlow<AuthenticationState<FirebaseUser>?> = _loginFlow

    private val _register = MutableStateFlow<AuthenticationState<*>?>(null)
    val register: StateFlow<AuthenticationState<*>?> = _register

    private val _forgotPassword = MutableStateFlow<AuthenticationState<*>?>(null)
    val forgotPassword: StateFlow<AuthenticationState<*>?> = _forgotPassword

    private val _logout = MutableStateFlow<AuthenticationState<*>?>(null)
    val logout: StateFlow<AuthenticationState<*>?> = _logout

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = AuthenticationState.Success(repository.currentUser!!)
        }
    }

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginFlow.value = AuthenticationState.Loading
        _loginFlow.value = repository.loginUser(
            email,
            password
        )
    }

    fun register(
        email: String,
        password: String,
        user: User
    ) = viewModelScope.launch {
        _register.value = AuthenticationState.Loading
        _register.value = repository.registerUser(
            email = email,
            password = password,
            user = user
        )
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgotPassword.value = AuthenticationState.Loading
        _forgotPassword.value = repository.forgotPassword(email)
    }

     fun logout() = viewModelScope.launch {
        _logout.value = AuthenticationState.Loading
        _logout.value = repository.logout()
    }
}
