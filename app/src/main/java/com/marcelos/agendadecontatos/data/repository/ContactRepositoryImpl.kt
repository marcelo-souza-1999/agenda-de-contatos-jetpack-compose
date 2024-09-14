package com.marcelos.agendadecontatos.data.repository

import com.marcelos.agendadecontatos.domain.datasource.ContactDataSource
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import org.koin.core.annotation.Single

@Single
class ContactRepositoryImpl(
    private val contactDataSource: ContactDataSource
) : ContactRepository {

    override suspend fun getContacts() = contactDataSource.getContacts()

    override suspend fun getContact(contactId: Int) = contactDataSource.getContact(contactId)

    override suspend fun insertContact(contacts: ContactsViewData) =
        contactDataSource.insertContacts(contacts)

    override suspend fun deleteContact(idContact: Int) = contactDataSource.deleteContact(idContact)
}