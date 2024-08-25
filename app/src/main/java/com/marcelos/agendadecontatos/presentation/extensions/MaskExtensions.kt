package com.marcelos.agendadecontatos.presentation.extensions

fun phoneMask(phoneNumber: String): String {
    val digits = phoneNumber.filter { it.isDigit() }.take(11)
    val masked = when (digits.length) {
        in 0..2 -> digits
        in 3..5 -> "${digits.substring(0, 2)}${digits.substring(2)}"
        in 6..10 -> "${digits.substring(0, 2)}${digits.substring(2, 6)}${digits.substring(6)}"
        else -> "${digits.substring(0, 2)}${digits.substring(2, 11)}"
    }
    return masked
}

fun ageMask(enteredText: String): String {
    return enteredText.filter { it.isDigit() }
}