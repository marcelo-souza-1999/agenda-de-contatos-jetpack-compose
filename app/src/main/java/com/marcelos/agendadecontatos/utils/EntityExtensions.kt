package com.marcelos.agendadecontatos.utils

import com.marcelos.agendadecontatos.data.entitys.ContactEntity
import com.marcelos.agendadecontatos.domain.model.ContactViewData

fun List<ContactEntity>.toContactViewData() = map {
    ContactViewData(
        id = it.id,
        image = it.image,
        name = it.name,
        surname = it.surname,
        age = it.age,
        phone = it.phone
    )
}

fun ContactViewData.toContactEntity() = ContactEntity(
    id = id,
    image = image,
    name = name,
    surname = surname,
    age = age,
    phone = phone
)