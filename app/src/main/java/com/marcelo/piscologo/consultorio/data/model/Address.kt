package com.marcelo.piscologo.consultorio.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @PropertyName("id")
    var id: String,
    @PropertyName("street")
    val street: String,
    @PropertyName("district")
    val district: String,
    @PropertyName("number")
    val number: String,
    @PropertyName("city")
    val city: String,
    @PropertyName("state")
    val state: String,
    @PropertyName("cep")
    val cep: String,
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "")
}
