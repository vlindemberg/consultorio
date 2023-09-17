package com.marcelo.piscologo.consultorio.utils

import com.marcelo.piscologo.consultorio.data.model.User

object Session {

    private var user: User? = null

    fun getLoggedUser(): User? {
        return user
    }

    fun setLoggedUser(user: User) {
        this.user = user
    }
}