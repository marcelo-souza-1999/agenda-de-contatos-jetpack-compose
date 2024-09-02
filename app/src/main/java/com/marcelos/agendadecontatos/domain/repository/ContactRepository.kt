package com.marcelos.agendadecontatos.domain.repository

import com.marcelos.agendadecontatos.domain.model.ContactViewData
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    suspend fun getContacts(): Flow<List<ContactViewData>>

    suspend fun insertContact(contacts: ContactViewData)

    suspend fun deleteContact(idContact: Int)
}