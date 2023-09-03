package com.marcelo.piscologo.consultorio.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.domain.repository.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun fetchPatients(userId: String) = viewModelScope.launch {
        _patients.value = PatientState.Loading
        _patients.value = patientRepository.getPatients(userId)
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