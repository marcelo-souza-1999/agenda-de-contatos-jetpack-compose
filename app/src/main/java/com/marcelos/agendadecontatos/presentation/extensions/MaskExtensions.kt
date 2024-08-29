package com.marcelos.agendadecontatos.presentation.extensions

fun String.phoneMask(): String {
    val digits = this.filter { it.isDigit() }.take(11)
    return when (digits.length) {
        in 0..2 -> digits
        in 3..5 -> "${digits.substring(0, 2)}${digits.substring(2)}"
        in 6..10 -> "${digits.substring(0, 2)}${digits.substring(2, 6)}${digits.substring(6)}"
        else -> "${digits.substring(0, 2)}${digits.substring(2, 11)}"
    }
}

fun String.ageMask(): String {
    return this.filter { it.isDigit() }
}