package com.marcelos.agendadecontatos.data.repository

import com.marcelos.agendadecontatos.domain.datasource.ContactDataSource
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactRepositoryImplTest {

    private val contactDataSource = mockk<ContactDataSource>(relaxed = true)
    private val contactRepository = ContactRepositoryImpl(contactDataSource)

    @Test
    fun `when getContacts is called, it should return a list of ContactsViewData`() = runBlocking {
        val contactsViewDataList = listOf(
            ContactsViewData(
                id = 1,
                imagePath = "path1",
                name = "John",
                surname = "Doe",
                age = 30,
                phone = "1234567890"
            ),
            ContactsViewData(
                id = 2,
                imagePath = "path2",
                name = "Jane",
                surname = "Doe",
                age = 28,
                phone = "0987654321"
            )
        )

        coEvery { contactDataSource.getContacts() } returns flowOf(contactsViewDataList)

        val result = contactRepository.getContacts().toList().flatten()
        assertEquals(contactsViewDataList, result)
        coVerify { contactDataSource.getContacts() }
    }

    @Test
    fun `when getContact is called, it should return a ContactViewData`() = runBlocking {
        val contactId = 1
        val contactViewData = ContactsViewData(
            id = contactId,
            imagePath = "path1",
            name = "John",
            surname = "Doe",
            age = 30,
            phone = "1234567890"
        )

        coEvery { contactDataSource.getContact(contactId) } returns flowOf(listOf(contactViewData))

        val result = contactRepository.getContact(contactId).toList().flatten()

        assertEquals(listOf(contactViewData), result)
        coVerify { contactDataSource.getContact(contactId) }
    }

    @Test
    fun `when insertContact is called, it should insert a contact in the data source`() = runBlocking {
        val contactsViewData = ContactsViewData(
            id = 1,
            imagePath = "path1",
            name = "John",
            surname = "Doe",
            age = 30,
            phone = "1234567890"
        )

        contactRepository.insertContact(contactsViewData)

        coVerify { contactDataSource.insertContacts(contactsViewData) }
    }

    @Test
    fun `when deleteContact is called, it should delete a contact by id from the data source`() = runBlocking {
        val contactId = 1

        contactRepository.deleteContact(contactId)

        coVerify { contactDataSource.deleteContact(contactId) }
    }
}