package com.marcelo.piscologo.consultorio.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Patient(
    @PropertyName("id")
    var id: String,
    @PropertyName("name")
    val name: String,
    @PropertyName("birthDate")
    val birthDate: String,
    @PropertyName("age")
    val age: String,
    @PropertyName("civilStatus")
    val civilStatus: String,
    @PropertyName("gender")
    val gender: String,
    @PropertyName("address")
    val address: Address,
    @PropertyName("phoneNumber")
    val phoneNumber: String,
    @PropertyName("secondaryPhoneNumber")
    val secondaryPhoneNumber: String,
    @PropertyName("email")
    val email: String,
    @PropertyName("anamnesis")
    val anamnesis: Anamnesis,
    @PropertyName("userId")
    val userId: String,
) : Parcelable {
    constructor() : this(
        id = "",
        name = "",
        birthDate = "",
        age = "",
        civilStatus = "",
        gender = "",
        address = Address(),
        phoneNumber = "",
        secondaryPhoneNumber = "",
        email = "",
        anamnesis = Anamnesis(),
        userId = ""
    )
}
