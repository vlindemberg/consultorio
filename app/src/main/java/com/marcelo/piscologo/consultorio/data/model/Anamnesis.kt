package com.marcelo.piscologo.consultorio.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Anamnesis(
    @PropertyName("id") var id: String
) : Parcelable {
    constructor() : this("")
}
