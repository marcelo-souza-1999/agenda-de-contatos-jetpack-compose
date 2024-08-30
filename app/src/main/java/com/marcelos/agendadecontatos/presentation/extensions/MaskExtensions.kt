package com.marcelos.agendadecontatos.presentation.extensions

private const val ZERO = 0
private const val MAX_DIGITS = 11
private const val AREA_CODE_LENGTH = 2
private const val PREFIX_MIN_LENGTH = 3
private const val PREFIX_MAX_LENGTH = 5
private const val PREFIX_FULL_LENGTH = 6
private const val SUFFIX_LENGTH = 10

fun String.phoneMask(): String {
    val digits = this.filter { it.isDigit() }.take(MAX_DIGITS)
    return when (digits.length) {
        in ZERO..AREA_CODE_LENGTH -> digits
        in PREFIX_MIN_LENGTH..PREFIX_MAX_LENGTH ->
            "${digits.substring(0, AREA_CODE_LENGTH)}${digits.substring(AREA_CODE_LENGTH)}"

        in PREFIX_FULL_LENGTH..SUFFIX_LENGTH ->
            "${digits.substring(0, AREA_CODE_LENGTH)}${
                digits.substring(
                    AREA_CODE_LENGTH,
                    PREFIX_FULL_LENGTH
                )
            }${digits.substring(PREFIX_FULL_LENGTH)}"

        else ->
            "${digits.substring(0, AREA_CODE_LENGTH)}${
                digits.substring(
                    AREA_CODE_LENGTH,
                    MAX_DIGITS
                )
            }"
    }
}

fun String.ageMask(): String {
    return this.filter { it.isDigit() }
}