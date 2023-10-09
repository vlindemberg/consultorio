package com.marcelo.piscologo.consultorio.data.repository

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.marcelo.piscologo.consultorio.data.extensions.await
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import com.marcelo.piscologo.consultorio.presentation.authentication.AuthenticationState
import com.marcelo.piscologo.consultorio.utils.FireStoreCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storageReference: StorageReference
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun loginUser(
        email: String,
        password: String,
    ): AuthenticationState<*> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = getLoggedUser(result.user?.uid.orEmpty())
            AuthenticationState.Success(user)
        } catch (e: Exception) {
            e.printStackTrace()
            AuthenticationState.Failure(e)
        }
    }

    override suspend fun validateSession(userId: String): AuthenticationState<*> {
        return try {
            val user = database.collection(FireStoreCollection.USER).document(userId).get().await()
            AuthenticationState.Success(user.toObject(User::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            AuthenticationState.Failure(e)
        }
    }

    private suspend fun getLoggedUser(userId: String): User? {
        return try {
            val user = database.collection(FireStoreCollection.USER).document(userId).get().await()
            user.toObject(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            User()
        }
    }

    override suspend fun registerUser(
        email: String, password: String, user: User
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
        val document = database.collection(FireStoreCollection.USER).document(user.id.orEmpty())
        try {
            document.set(user).await()
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

    override suspend fun uploadPhoto(photoUri: Uri, user: User): AuthenticationState<*> {
        var state: AuthenticationState<*> = AuthenticationState.Failure(IllegalArgumentException())
        try {
            val uri: Uri = withContext(Dispatchers.IO) {
                storageReference
                    .child("Users")
                    .child(Date().time.toString())
                    .putFile(photoUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            user.photo = uri.toString()
            updateUserInfo(user) {
                state = when (it) {
                    is AuthenticationState.Success -> AuthenticationState.Success(user)
                    is AuthenticationState.Failure -> AuthenticationState.Failure(it.exception)
                    else -> AuthenticationState.Failure(IllegalArgumentException())
                }
            }
        } catch (e: FirebaseException) {
            state = AuthenticationState.Failure(e)
        } catch (e: Exception) {
            state = AuthenticationState.Failure(e)
        }
        return state
    }

    override suspend fun logout(): AuthenticationState<*> {
        return try {
            withContext(Dispatchers.IO) {
                Thread.sleep(1500)
                auth.signOut()
                AuthenticationState.Success(true)
            }
        } catch (e: Exception) {
            AuthenticationState.Failure(e)
        }
    }
}
