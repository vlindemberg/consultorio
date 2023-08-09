package com.marcelo.piscologo.consultorio.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.piscologo.consultorio.data.extensions.await
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import com.marcelo.piscologo.consultorio.presentation.authentication.AuthenticationState
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
    ): AuthenticationState<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthenticationState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            AuthenticationState.Failure(e)
        }
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        user: User
    ): AuthenticationState<*> {
        var state: AuthenticationState<*> = AuthenticationState.Failure(IllegalArgumentException())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            user.id = result.user?.uid ?: ""
            updateUserInfo(user) {
                state = when (it) {
                    is AuthenticationState.Success -> AuthenticationState.Success(user)
                    is AuthenticationState.Failure -> AuthenticationState.Failure(it.exception)
                    else -> AuthenticationState.Failure(IllegalArgumentException())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            state = AuthenticationState.Failure(e)
        }
        return state
    }

    override suspend fun updateUserInfo(user: User, result: (AuthenticationState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        try {
            document
                .set(user)
                .await()
            result.invoke(AuthenticationState.Success("User Registered!"))
        } catch (e: Exception) {
            e.printStackTrace()
            result.invoke(AuthenticationState.Failure(e))
        }
    }

    override suspend fun forgotPassword(email: String): AuthenticationState<*> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthenticationState.Success("Email Enviado, verifique sua caixa de entrada!")
        } catch (e: Exception) {
            e.printStackTrace()
            AuthenticationState.Failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}
