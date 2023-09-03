package com.marcelo.piscologo.consultorio.domain.repository

import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.presentation.patient.PatientState

interface PatientRepository {
    suspend fun addPatient(patient: Patient): PatientState<*>
    suspend fun updatePatient(patient: Patient): PatientState<*>
    suspend fun deletePatient(patientId: String): PatientState<*>
    suspend fun getPatients(userId: String): PatientState<*>
}
