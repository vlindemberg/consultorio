package com.marcelo.piscologo.consultorio.data.model

data class Address(
    val id: String,
    val street: String,
    val district: String,
    val phoneNumber: String,
    val secondaryPhoneNumber: String?,
    val city: String,
    val state: String,
    val cep: String,
)
