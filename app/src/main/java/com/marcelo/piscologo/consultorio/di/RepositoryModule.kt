package com.marcelo.piscologo.consultorio.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.piscologo.consultorio.data.repository.AuthRepositoryImpl
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
    ): AuthRepository {
        return AuthRepositoryImpl(auth = auth, database = database)
    }
}