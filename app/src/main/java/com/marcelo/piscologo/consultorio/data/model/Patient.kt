package com.marcelo.piscologo.consultorio.data.model

data class Patient(
    var id: String,
    val name: String,
    val birthDate: String,
    val age: String,
    val civilStatus: String,
    val gender: String,
    val address: Address,
    val phoneNumber: String,
    val email: String,
    val anamnesis: Anamnesis?,
    val psychologist: User,
)
