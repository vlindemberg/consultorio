package com.marcelo.piscologo.consultorio.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.marcelo.piscologo.consultorio.data.repository.AuthRepositoryImpl
import com.marcelo.piscologo.consultorio.data.repository.PatientRepositoryImpl
import com.marcelo.piscologo.consultorio.domain.repository.AuthRepository
import com.marcelo.piscologo.consultorio.domain.repository.PatientRepository
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
    fun provideAuthRepository(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
        storageReference: StorageReference,
    ): AuthRepository {
        return AuthRepositoryImpl(
            auth = auth,
            database = database,
            storageReference = storageReference
        )
    }

    @Provides
    @Singleton
    fun providePatientRepository(
        database: FirebaseFirestore,
    ): PatientRepository {
        return PatientRepositoryImpl(database = database)
    }
}