package com.marcelo.piscologo.consultorio.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.presentation.authentication.login.LoginState
import com.marcelo.piscologo.consultorio.presentation.authentication.register.RegisterState

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun loginUser(email: String, password: String) : LoginState<FirebaseUser>
    suspend fun registerUser(email: String, password: String, user: User): RegisterState<*>
    suspend fun updateUserInfo(user: User, result: (RegisterState<String>) -> Unit)
    suspend fun forgotPassword(email: String)
    suspend fun logout()
}

