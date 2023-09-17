package com.marcelo.piscologo.consultorio.utils

private const val REGEX_ONLY_NUMBERS = "[^0-9]"

fun String.unmaskOnlyNumbers() = this.replace(Regex(REGEX_ONLY_NUMBERS), "")