package com.marcelos.agendadecontatos.data.datasource

import com.marcelos.agendadecontatos.data.dao.ContactDao
import com.marcelos.agendadecontatos.domain.datasource.ContactDataSource
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.utils.toContactEntity
import com.marcelos.agendadecontatos.utils.toContactViewData
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ContactDataSourceImpl(
    private val contactDao: ContactDao
) : ContactDataSource {

    override suspend fun getContacts() = contactDao.getContacts().map {
        it.toContactViewData()
    }

    override suspend fun getContact(contactId: Int) = contactDao.getContactById(contactId).map {
        it.toContactViewData()
    }

    override suspend fun insertContacts(contacts: ContactsViewData) = contactDao.insertContact(
        contacts.toContactEntity()
    )

    override suspend fun deleteContact(idContact: Int) = contactDao.deleteContact(idContact)
}