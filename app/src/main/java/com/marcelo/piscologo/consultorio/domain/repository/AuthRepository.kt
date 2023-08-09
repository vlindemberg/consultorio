package com.marcelo.piscologo.consultorio.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.presentation.authentication.AuthenticationState

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun loginUser(email: String, password: String) : AuthenticationState<FirebaseUser>
    suspend fun registerUser(email: String, password: String, user: User): AuthenticationState<*>
    suspend fun updateUserInfo(user: User, result: (AuthenticationState<String>) -> Unit)
    suspend fun forgotPassword(email: String): AuthenticationState<*>
    suspend fun logout()
}
