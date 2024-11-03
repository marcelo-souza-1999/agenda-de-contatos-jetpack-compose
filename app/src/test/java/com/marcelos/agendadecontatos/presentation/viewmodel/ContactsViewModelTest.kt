package com.marcelos.agendadecontatos.presentation.viewmodel

import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.usecase.DeleteContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactsUseCase
import com.marcelos.agendadecontatos.domain.usecase.SaveContactUseCase
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {

    private val saveContactUseCase = mockk<SaveContactUseCase>(relaxed = true)
    private val getContactsUseCase = mockk<GetContactsUseCase>(relaxed = true)
    private val getContactUseCase = mockk<GetContactUseCase>(relaxed = true)
    private val deleteContactUseCase = mockk<DeleteContactUseCase>(relaxed = true)
    private lateinit var viewModel: ContactsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ContactsViewModel(
            saveContactUseCase,
            getContactsUseCase,
            getContactUseCase,
            deleteContactUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getContacts is called, viewStateGetContacts should be updated to success`() =
        runTest {
            // Arrange
            val contacts = listOf(
                ContactsViewData(
                    id = 1,
                    imagePath = "PhotoUrl",
                    name = "Marcelo",
                    surname = "Souza",
                    age = 25,
                    phone = "12996259393"
                )
            )
            coEvery { getContactsUseCase() } returns flowOf(State.Success(contacts))

            // Act
            viewModel.getContacts()

            // Assert
            val states = mutableListOf<State<List<ContactsViewData>>>()
            val job = launch(testDispatcher) { viewModel.viewStateGetContacts.toList(states) }
            advanceUntilIdle()

            // Como o estado inicial já é Loading, verificamos apenas a transição final
            assertEquals(State.Success(contacts), states.last())

            job.cancel()
        }

    @Test
    fun `when saveContact is called, viewStateSaveContact should be updated to success`() =
        runTest {
            // Arrange
            val contact = ContactsViewData(
                id = 1,
                imagePath = "PhotoUrl",
                name = "Marcelo",
                surname = "Souza",
                age = 25,
                phone = "12996259393"
            )
            coEvery { saveContactUseCase(any(), any(), any(), any(), any(), any()) } returns flowOf(
                State.Success(contact)
            )

            // Act
            viewModel.saveContact(
                contactId = 1,
                imagePath = "PhotoUrl",
                name = "Marcelo",
                surname = "Souza",
                age = 25,
                phone = "12996259393"
            )

            // Assert
            val states = mutableListOf<State<ContactsViewData>>()
            val job = launch(testDispatcher) { viewModel.viewStateSaveContact.toList(states) }
            advanceUntilIdle()

            assertEquals(State.Success(contact), states.last())

            job.cancel()
        }

    @Test
    fun `when deleteContact is called, viewStateDeleteContact should be updated to success`() =
        runTest {
            // Arrange
            coEvery { deleteContactUseCase(1) } returns flowOf(State.Success(Unit))

            // Act
            viewModel.deleteContact(1)

            // Assert
            val states = mutableListOf<State<Unit>>()
            val job = launch(testDispatcher) { viewModel.viewStateDeleteContact.toList(states) }
            advanceUntilIdle()

            assertEquals(State.Success(Unit), states.last())

            job.cancel()
        }


    @Test
    fun `when validateFields is called with empty fields, it should return false and update errors`() {
        // Act
        val isValid = viewModel.validateFields()

        // Assert
        assertFalse(isValid)
        assertTrue(viewModel.nameError.value)
        assertTrue(viewModel.surnameError.value)
        assertTrue(viewModel.ageError.value)
        assertTrue(viewModel.phoneError.value)
    }

    @Test
    fun `when resetDeleteContactState is called, viewStateDeleteContact should be reset to Loading`() =
        runTest {
            // Act
            viewModel.resetDeleteContactState()

            // Assert
            assertTrue(viewModel.viewStateDeleteContact.value is State.Loading)
        }

    @Test
    fun `when validateFields is called with valid data, it should return true and no error flags should be set`() {
        // Arrange
        viewModel.updateName("Marcelo")
        viewModel.updateSurname("Souza")
        viewModel.updateAge("25")
        viewModel.updatePhone("12996259393")

        // Act
        val isValid = viewModel.validateFields()

        // Assert
        assertTrue(isValid)
        assertFalse(viewModel.nameError.value)
        assertFalse(viewModel.surnameError.value)
        assertFalse(viewModel.ageError.value)
        assertFalse(viewModel.phoneError.value)
    }

    @Test
    fun `when getContact is called, viewStateGetContacts should be updated to success with the contact data`() =
        runTest {
            // Arrange
            val contact = ContactsViewData(
                id = 1,
                imagePath = "PhotoUrl",
                name = "Marcelo",
                surname = "Souza",
                age = 25,
                phone = "12996259393"
            )
            coEvery { getContactUseCase(1) } returns flowOf(State.Success(listOf(contact)))

            // Act
            viewModel.getContact(1)

            // Assert
            val states = mutableListOf<State<List<ContactsViewData>>>()
            val job = launch(testDispatcher) { viewModel.viewStateGetContacts.toList(states) }
            advanceUntilIdle()

            assertEquals(State.Success(listOf(contact)), states.last())

            job.cancel()
        }

    @Test
    fun `when updateName is called with a non-empty name, name state should be updated and nameError should be false`() =
        runTest {
            // Act
            viewModel.updateName("Marcelo")

            // Assert
            assertEquals("Marcelo", viewModel.name.value)
            assertFalse(viewModel.nameError.value)
        }

    @Test
    fun `when updateSurname is called with a non-empty surname, surname state should be updated and surnameError should be false`() =
        runTest {
            // Act
            viewModel.updateSurname("Souza")

            // Assert
            assertEquals("Souza", viewModel.surname.value)
            assertFalse(viewModel.surnameError.value)
        }


    @Test
    fun `when updateAge is called with a non-empty age, age state should be updated and ageError should be false`() =
        runTest {
            // Act
            viewModel.updateAge("25")

            // Assert
            assertEquals("25", viewModel.age.value)
            assertFalse(viewModel.ageError.value)
        }


    @Test
    fun `when updatePhone is called with a non-empty phone, phone state should be updated and phoneError should be false`() =
        runTest {
            // Act
            viewModel.updatePhone("12996259393")

            // Assert
            assertEquals("12996259393", viewModel.phone.value)
            assertFalse(viewModel.phoneError.value)
        }

}