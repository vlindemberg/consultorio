package com.marcelo.piscologo.consultorio.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.marcelo.piscologo.consultorio.data.extensions.await
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.domain.repository.PatientRepository
import com.marcelo.piscologo.consultorio.presentation.patient.PatientState
import com.marcelo.piscologo.consultorio.utils.FireStoreCollection
import com.marcelo.piscologo.consultorio.utils.FireStoreDocumentField

class PatientRepositoryImpl(
    private val database: FirebaseFirestore
) : PatientRepository {
    override suspend fun addPatient(patient: Patient): PatientState<*> {
        return try {
            val document = database.collection(FireStoreCollection.PATIENT).document()
            patient.id = document.id
            document.set(patient).await()
            PatientState.Success(Pair(patient, "Paciente cadastrado!"))
        } catch (e: Exception) {
            e.printStackTrace()
            PatientState.Failure("Erro ao cadastrar paciente!")
        }
    }

    override suspend fun updatePatient(patient: Patient): PatientState<*> {
        return try {
            val document = database.collection(FireStoreCollection.PATIENT).document(patient.id)
            document.set(patient).await()
            PatientState.Success("Paciente atualizado!")
        } catch (e: Exception) {
            e.printStackTrace()
            PatientState.Failure("Erro ao atualizar paciente!")
        }
    }

    override suspend fun deletePatient(patientId: String): PatientState<*> {
        return try {
            val document = database.collection(FireStoreCollection.PATIENT).document(patientId)
            document.delete().await()
            PatientState.Success("Paciente deletado!")
        } catch (e: Exception) {
            e.printStackTrace()
            PatientState.Failure("Erro ao deletar paciente!")
        }
    }

    override suspend fun getPatients(userId: String): PatientState<*> {
        return try {
            val results = database.collection(FireStoreCollection.PATIENT)
                .whereEqualTo(FireStoreDocumentField.USER_ID, userId)
                .orderBy(FireStoreDocumentField.NAME, Query.Direction.DESCENDING)
                .get().await()
            val patients = arrayListOf<Patient>()
            for (result in results) {
                val patient = result.toObject(Patient::class.java)
                patients.add(patient)
            }
            PatientState.Success("Lista de pacientes carregada!", patients = patients)
        } catch (e: Exception) {
            e.printStackTrace()
            PatientState.Failure("Erro ao listar pacientes!")
        }
    }
}