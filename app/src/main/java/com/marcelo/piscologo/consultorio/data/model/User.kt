package com.marcelo.piscologo.consultorio.data.model

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @PropertyName("id")
    var id: String,
    @PropertyName("name")
    val name: String,
    @PropertyName("surname")
    val surname: String,
    @PropertyName("email")
    val email: String,
    @PropertyName("photo")
    var photo: String?,
) : Parcelable {
    constructor() : this("", "", "", "", null)
}
