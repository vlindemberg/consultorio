package com.marcelo.piscologo.consultorio.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.domain.repository.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _patients = MutableStateFlow<PatientState<*>?>(null)
    val patients: StateFlow<PatientState<*>?> = _patients

    private val _newPatient = MutableStateFlow<PatientState<*>?>(null)
    val newPatient: StateFlow<PatientState<*>?> = _newPatient

    private val _removePatient = MutableStateFlow<PatientState<*>?>(null)
    val removePatient: StateFlow<PatientState<*>?> = _removePatient

    fun fetchPatients(userId: String) = viewModelScope.launch {
        _patients.value = PatientState.Loading
        _patients.value = patientRepository.getPatients(userId)
    }

    fun addPatient(patient: Patient) = viewModelScope.launch {
        _newPatient.value = PatientState.Loading
        _newPatient.value = patientRepository.addPatient(patient)
    }

    fun removePatient(patientId: String) = viewModelScope.launch {
        _removePatient.value = PatientState.Loading
        _removePatient.value = patientRepository.deletePatient(patientId)
    }

    fun filterList(
        name: String?,
        userListLocation: List<Patient>
    ): List<Patient> {
        val filteredList = ArrayList<Patient>()
        if (name != null) {
            for (i in userListLocation) {
                if (i.name.lowercase(Locale.ROOT).contains(name)) {
                    filteredList.add(i)
                }
            }
        }
        return filteredList
    }
}