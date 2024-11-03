package com.marcelos.agendadecontatos.presentation.ui.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.usecase.DeleteContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactsUseCase
import com.marcelos.agendadecontatos.domain.usecase.SaveContactUseCase
import com.marcelos.agendadecontatos.presentation.ui.activity.MainActivity
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactsViewModel
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import com.marcelos.agendadecontatos.utils.formattedPhone
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class ShowContactsScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var contactsFlow: MutableStateFlow<State<List<ContactsViewData>>>
    private lateinit var deleteContactFlow: MutableStateFlow<State<Unit>>
    private lateinit var viewModel: ContactsViewModel

    private val saveContactUseCase = mockk<SaveContactUseCase>(relaxed = true)
    private val getContactsUseCase = mockk<GetContactsUseCase>(relaxed = true)
    private val getContactUseCase = mockk<GetContactUseCase>(relaxed = true)
    private val deleteContactUseCase = mockk<DeleteContactUseCase>(relaxed = true)

    private val contactId = 1
    private val sampleContactList = listOf(
        ContactsViewData(
            id = contactId,
            name = "John", surname = "Doe", age = 30, phone = "12123456789", imagePath = null
        )
    )

    @Before
    fun setup() {
        contactsFlow = MutableStateFlow(State.Loading())
        deleteContactFlow = MutableStateFlow(State.Loading())

        viewModel = ContactsViewModel(
            saveContactUseCase, getContactsUseCase, getContactUseCase, deleteContactUseCase
        ).apply {
            viewStateGetContacts = contactsFlow
            viewStateDeleteContact = deleteContactFlow
        }

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(module {
                    viewModel { viewModel }
                })
            }
        }
    }

    private fun setContent() = composeTestRule.setContent {
        ShowContacts(
            navController = rememberNavController(),
            viewModel = viewModel
        )
    }

    @Test
    fun checkTopBarTitleIsCorrect() {
        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.onNodeWithText("Agenda de Contatos").assertIsDisplayed()
    }

    @Test
    fun checkAllElementsAreDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)
        composeTestRule.onNodeWithTag("topAppBarComponent").assertIsDisplayed()
        composeTestRule.onNodeWithTag("floatingActionBtn").assertIsDisplayed()
    }

    @Test
    fun testEmptyContactsListDisplaysNoContactsMessage() {
        contactsFlow.value = State.Success(emptyList())
        setContent()
        composeTestRule.onNodeWithText("Nenhum contato encontrado").assertIsDisplayed()
    }

    @Test
    fun testNotEmptyContactsListDisplaysContactItems() {
        val contactList = listOf(
            ContactsViewData(
                name = "John", surname = "Doe", age = 30, phone = "12123456789", imagePath = null
            ), ContactsViewData(
                name = "Jane", surname = "Smith", age = 28, phone = "12996259393", imagePath = null
            )
        )
        contactsFlow.value = State.Success(contactList)

        setContent()

        composeTestRule.onAllNodesWithTag("cardContact").assertCountEquals(2)

        contactList.forEach { contact ->
            val fullName = "Nome: ${contact.name} ${contact.surname}"
            val ageText = "Idade: ${contact.age}"
            val phoneText = "Número: ${contact.phone.formattedPhone()}"

            composeTestRule.onNodeWithText(fullName).assertIsDisplayed()
            composeTestRule.onNodeWithText(ageText).assertIsDisplayed()
            composeTestRule.onNodeWithText(phoneText).assertIsDisplayed()
        }
    }

    @Test
    fun testFloatingActionButtonNavigatesToSaveContactScreen() {
        var navigatedRoute: String? = null

        composeTestRule.setContent {
            val navController = rememberNavController().apply {
                addOnDestinationChangedListener { _, destination, _ ->
                    navigatedRoute = destination.route
                }
            }
            NavHost(navController = navController, startDestination = Routes.ShowContacts.route) {
                composable(Routes.ShowContacts.route) {
                    ShowContacts(
                        navController = navController, viewModel = viewModel
                    )
                }
                composable(Routes.SaveContact.route) {

                }
            }
        }

        composeTestRule.onNodeWithTag("floatingActionBtn").performClick()

        assertEquals(Routes.SaveContact.route, navigatedRoute)
    }

    @Test
    fun testEditIconNavigatesToUpdateContactScreen() {
        val contactList = listOf(
            ContactsViewData(
                id = 1,
                name = "John", surname = "Doe", age = 30, phone = "12123456789", imagePath = null
            )
        )
        contactsFlow.value = State.Success(contactList)

        var navigatedRoute: String? = null

        composeTestRule.setContent {
            val navController = rememberNavController().apply {
                addOnDestinationChangedListener { _, destination, _ ->
                    navigatedRoute = destination.route
                }
            }
            NavHost(navController = navController, startDestination = Routes.ShowContacts.route) {
                composable(Routes.ShowContacts.route) {
                    ShowContacts(
                        navController = navController, viewModel = viewModel
                    )
                }
                composable("${Routes.UpdateContact.route}/1") {}
            }
        }

        composeTestRule.onNodeWithTag("iconEditContact").performClick()

        assertEquals("${Routes.UpdateContact.route}/1", navigatedRoute)
    }

    @Test
    fun testDeleteContactDisplayWarningDeleteContactDialog() {
        contactsFlow.value = State.Success(sampleContactList)

        setContent()

        composeTestRule.onNodeWithTag("iconDeleteContact").performClick()

        deleteContactFlow.value = State.Success(Unit)

        composeTestRule.onNodeWithText("Excluindo contato").assertIsDisplayed()
    }

    @Test
    fun testDeleteContactDisplaysSuccessDialogAfterConfirmation() {
        contactsFlow.value = State.Success(sampleContactList)

        setContent()

        composeTestRule.onNodeWithTag("iconDeleteContact").performClick()

        deleteContactFlow.value = State.Success(Unit)

        composeTestRule.onNodeWithText("Excluindo contato").assertIsDisplayed()

        composeTestRule.onNodeWithText("Excluir").performClick()

        deleteContactFlow.value = State.Success(Unit)

        composeTestRule.onNodeWithText("Contato excluído").assertIsDisplayed()
    }

    @Test
    fun testDeleteContactDisplaysErrorDialogOnFailure() {
        contactsFlow.value = State.Success(sampleContactList)

        setContent()

        composeTestRule.onNodeWithTag("iconDeleteContact").performClick()

        deleteContactFlow.value = State.Error(
            Throwable("Erro ao deletar contato")
        )

        composeTestRule.onNodeWithText("Excluir contato").assertIsDisplayed()

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        deleteContactFlow.value = State.Error(
            Throwable("Erro ao deletar contato")
        )

        composeTestRule.onNodeWithText("Excluir contato").assertIsDisplayed()
    }
}
