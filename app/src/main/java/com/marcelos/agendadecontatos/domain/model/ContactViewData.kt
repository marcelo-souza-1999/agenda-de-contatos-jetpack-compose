package com.marcelos.agendadecontatos.domain.model

import java.util.UUID

data class ContactViewData(
    val id: UUID,
    val image: ByteArray?,
    val name: String,
    val surname: String,
    val age: Int,
    val phone: String
)
