package com.marcelos.agendadecontatos.data.datasource

import com.marcelos.agendadecontatos.data.dao.ContactDao
import com.marcelos.agendadecontatos.data.entitys.ContactEntity
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.utils.toContactEntity
import com.marcelos.agendadecontatos.utils.toContactViewData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactDataSourceImplTest {

    private val contactDao = mockk<ContactDao>(relaxed = true)
    private val contactDataSource = ContactDataSourceImpl(contactDao)

    @Test
    fun `when getContacts is called, it should return a list of ContactsViewData`() = runBlocking {
        val contactEntityList = listOf(
            ContactEntity(
                id = 1,
                imagePath = "path1",
                name = "John",
                surname = "Doe",
                age = 30,
                phone = "1234567890"
            ), ContactEntity(
                id = 2,
                imagePath = "path2",
                name = "Jane",
                surname = "Doe",
                age = 28,
                phone = "0987654321"
            )
        )

        coEvery { contactDao.getContacts() } returns flowOf(contactEntityList)
        val result = contactDataSource.getContacts().toList()
        assertEquals(listOf(contactEntityList.toContactViewData()), result)
    }

    @Test
    fun `when getContact is called, it should return a ContactViewData`() = runBlocking {
        val contactId = 1
        val contactEntity = listOf(
            ContactEntity(
                id = contactId,
                imagePath = "path1",
                name = "John",
                surname = "Doe",
                age = 30,
                phone = "1234567890"
            )
        )

        coEvery { contactDao.getContactById(contactId) } returns flowOf(contactEntity)
        val result = contactDataSource.getContact(contactId).toList()
        assertEquals(listOf(contactEntity.toContactViewData()), result)
    }

    @Test
    fun `when insertContacts is called, it should insert a contact in the dao`() = runBlocking {
        val contactsViewData = ContactsViewData(
            id = 1,
            imagePath = "path1",
            name = "John",
            surname = "Doe",
            age = 30,
            phone = "1234567890"
        )

        val contactEntity = contactsViewData.toContactEntity()
        contactDataSource.insertContacts(contactsViewData)
        coVerify { contactDao.insertContact(contactEntity) }
    }

    @Test
    fun `when deleteContact is called, it should delete a contact by id from the dao`() = runBlocking {
        val contactId = 1

        contactDataSource.deleteContact(contactId)

        coVerify { contactDao.deleteContact(contactId) }
    }
}
