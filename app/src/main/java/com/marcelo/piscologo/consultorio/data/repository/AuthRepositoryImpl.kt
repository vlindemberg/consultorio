package com.marcelo.piscologo.consultorio.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.piscologo.consultorio.data.extensions.await
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import com.marcelo.piscologo.consultorio.presentation.authentication.login.LoginState
import com.marcelo.piscologo.consultorio.presentation.authentication.register.RegisterState
import com.marcelo.piscologo.consultorio.utils.FireStoreCollection

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun loginUser(
        email: String,
        password: String,
    ): LoginState<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            LoginState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            LoginState.Failure(e)
        }
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        user: User
    ): RegisterState<*> {
        var state: RegisterState<*> = RegisterState.Failure(IllegalArgumentException())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            user.id = result.user?.uid ?: ""
            updateUserInfo(user) {
                state = when (it) {
                    is RegisterState.Success -> RegisterState.Success(user)
                    is RegisterState.Failure -> RegisterState.Failure(it.exception)
                    else -> RegisterState.Failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            state = RegisterState.Failure(e)
        }
        return state
    }

    override suspend fun updateUserInfo(user: User, result: (RegisterState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        try {
            document
                .set(user)
                .await()
            result.invoke(RegisterState.Success("User Registered!"))
        } catch (e: Exception) {
            e.printStackTrace()
            result.invoke(RegisterState.Failure(e))
        }
    }

    override suspend fun forgotPassword(email: String) {
        try {
            auth.sendPasswordResetEmail(email).await()
            LoginState.Success("Email Send")
        } catch (e: Exception) {
            e.printStackTrace()
            LoginState.Failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}
