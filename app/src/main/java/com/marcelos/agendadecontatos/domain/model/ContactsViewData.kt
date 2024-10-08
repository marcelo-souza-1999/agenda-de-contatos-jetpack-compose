package com.marcelos.agendadecontatos.domain.model

data class ContactsViewData(
    val id: Int? = null,
    val imagePath: String?,
    val name: String,
    val surname: String,
    val age: Int,
    val phone: String
)
