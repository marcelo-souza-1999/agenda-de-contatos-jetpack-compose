package com.marcelos.agendadecontatos.utils

import com.marcelos.agendadecontatos.data.entitys.ContactEntity
import com.marcelos.agendadecontatos.domain.model.ContactsViewData

fun List<ContactEntity>.toContactViewData() = map {
    ContactsViewData(
        id = it.id,
        imagePath = it.imagePath,
        name = it.name,
        surname = it.surname,
        age = it.age,
        phone = it.phone
    )
}

fun ContactsViewData.toContactEntity() = ContactEntity(
    id = id,
    imagePath = imagePath,
    name = name,
    surname = surname,
    age = age,
    phone = phone
)