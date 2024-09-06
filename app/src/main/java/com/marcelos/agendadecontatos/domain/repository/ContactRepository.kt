package com.marcelos.agendadecontatos.domain.repository

import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    suspend fun getContacts(): Flow<List<ContactsViewData>>

    suspend fun insertContact(contacts: ContactsViewData)

    suspend fun deleteContact(idContact: Int)
}