package com.marcelo.piscologo.consultorio.presentation.authentication

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import com.marcelo.piscologo.consultorio.utils.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<AuthenticationState<*>?>(null)
    val loginFlow: StateFlow<AuthenticationState<*>?> = _loginFlow

    private val _validateSessionFlow = MutableStateFlow<AuthenticationState<*>?>(null)
    val validateSessionFlow: StateFlow<AuthenticationState<*>?> = _validateSessionFlow

    private val _register = MutableStateFlow<AuthenticationState<*>?>(null)
    val register: StateFlow<AuthenticationState<*>?> = _register

    private val _forgotPassword = MutableStateFlow<AuthenticationState<*>?>(null)
    val forgotPassword: StateFlow<AuthenticationState<*>?> = _forgotPassword

    private val _logout = MutableStateFlow<AuthenticationState<*>?>(null)
    val logout: StateFlow<AuthenticationState<*>?> = _logout

    private val _uploadPhoto = MutableStateFlow<AuthenticationState<*>?>(null)
    val uploadPhoto: StateFlow<AuthenticationState<*>?> = _uploadPhoto

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        repository.currentUser?.let { user ->
            validateSession(user.uid)
        }
    }

    private fun validateSession(userId: String) = viewModelScope.launch {
        _validateSessionFlow.value = AuthenticationState.Loading
        _validateSessionFlow.value = repository.validateSession(userId)
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

    fun uploadPhoto(photoUri: Uri, user: User) = viewModelScope.launch {
        _uploadPhoto.value = AuthenticationState.Loading
        _uploadPhoto.value = repository.uploadPhoto(photoUri, user)
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgotPassword.value = AuthenticationState.Loading
        _forgotPassword.value = repository.forgotPassword(email)
    }

    fun logout() = viewModelScope.launch {
        _logout.value = AuthenticationState.Loading
        _logout.value = repository.logout()
    }

    fun session(user: User) {
        Session.setLoggedUser(user)
    }
}
