package com.marcelo.piscologo.consultorio.presentation.patient

import com.marcelo.piscologo.consultorio.data.model.Patient

sealed class PatientState<out T> {
    object Loading : PatientState<Nothing>()
    data class Success<out T>(
        val message: T,
        val patients: List<Patient> = emptyList()
    ) : PatientState<T>()
    data class Failure(val errorMessage: String) : PatientState<Nothing>()
}