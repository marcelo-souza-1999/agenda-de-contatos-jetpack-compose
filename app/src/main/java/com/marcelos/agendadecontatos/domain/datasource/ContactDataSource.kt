package com.marcelos.agendadecontatos.domain.datasource

import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import kotlinx.coroutines.flow.Flow

interface ContactDataSource {

    suspend fun getContacts(): Flow<List<ContactsViewData>>

    suspend fun getContact(contactId: Int): Flow<List<ContactsViewData>>

    suspend fun insertContacts(contacts: ContactsViewData)

    suspend fun deleteContact(idContact: Int)
}