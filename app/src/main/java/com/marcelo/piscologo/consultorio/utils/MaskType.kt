package com.marcelo.piscologo.consultorio.utils

sealed class MaskType(pattern: String) : Mask(pattern) {

    object CPF : MaskType("###.###.###-##")
    object DATE : MaskType("##/##/####")
    object CEP : MaskType("#####-###")
    object PhoneNineWithDDD : MaskType("(##) #####-####")
    object PhoneEightWithoutDDD : MaskType("####-####")
}