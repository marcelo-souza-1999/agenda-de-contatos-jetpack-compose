package com.marcelos.agendadecontatos.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.usecase.DeleteContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactsUseCase
import com.marcelos.agendadecontatos.domain.usecase.SaveContactUseCase
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactsViewModel
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
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
class SaveContactScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var saveContactFlow: MutableStateFlow<State<ContactsViewData>>
    private lateinit var nameErrorFlow: MutableStateFlow<Boolean>
    private lateinit var surnameErrorFlow: MutableStateFlow<Boolean>
    private lateinit var ageErrorFlow: MutableStateFlow<Boolean>
    private lateinit var phoneErrorFlow: MutableStateFlow<Boolean>
    private lateinit var viewModel: ContactsViewModel

    private val saveContactUseCase = mockk<SaveContactUseCase>(relaxed = true)
    private val getContactsUseCase = mockk<GetContactsUseCase>(relaxed = true)
    private val getContactUseCase = mockk<GetContactUseCase>(relaxed = true)
    private val deleteContactUseCase = mockk<DeleteContactUseCase>(relaxed = true)

    private val contactId = 1
    private val sampleContactsData = ContactsViewData(
        id = contactId,
        name = "John", surname = "Doe", age = 30, phone = "12123456789", imagePath = null
    )

    @Before
    fun setup() {
        saveContactFlow = MutableStateFlow(State.Loading())
        nameErrorFlow = MutableStateFlow(false)
        surnameErrorFlow = MutableStateFlow(false)
        ageErrorFlow = MutableStateFlow(false)
        phoneErrorFlow = MutableStateFlow(false)

        viewModel = ContactsViewModel(
            saveContactUseCase,
            getContactsUseCase,
            getContactUseCase,
            deleteContactUseCase
        ).apply {
            viewStateSaveContact = saveContactFlow
            nameError = nameErrorFlow
            surnameError = surnameErrorFlow
            ageError = ageErrorFlow
            phoneError = phoneErrorFlow
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
        SaveContactScreen(navController = rememberNavController(), viewModel = viewModel)
    }

    @Test
    fun checkTopBarTitleIsCorrect() {
        setContent()

        composeTestRule.onNodeWithText("Salvar novo Contato").assertIsDisplayed()
    }

    @Test
    fun testSaveContactDisplaySuccessDialog() {
        saveContactFlow.value = State.Success(sampleContactsData)
        setContent()

        composeTestRule.onNodeWithText("Contato salvo").assertIsDisplayed()
    }

    @Test
    fun testSaveContactDisplaySuccessDialogNavigateOnOkClick() {
        var navigatedRoute: String? = null

        saveContactFlow.value = State.Success(sampleContactsData)

        composeTestRule.setContent {
            val navController = rememberNavController().apply {
                addOnDestinationChangedListener { _, destination, _ ->
                    navigatedRoute = destination.route
                }
            }
            NavHost(navController = navController, startDestination = Routes.SaveContact.route) {
                composable(Routes.SaveContact.route) {
                    SaveContactScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(Routes.ShowContacts.route) {
                    ShowContactsScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("Agenda de Contatos").assertIsDisplayed()

        assertEquals(Routes.ShowContacts.route, navigatedRoute)
    }

    @Test
    fun testSaveContactDisplayErrorDialog() {
        saveContactFlow.value = State.Error(Throwable("Erro ao salvar contato"))
        setContent()

        composeTestRule.onNodeWithText("Ocorreu um erro ao tentar salvar o contato!")
            .assertIsDisplayed()
    }

    @Test
    fun testSaveContactDisplaysErrorDialogAndRetryOnFailure() {
        saveContactFlow.value = State.Error(Throwable("Erro ao salvar contato"))
        setContent()

        composeTestRule.onNodeWithTag("inputName").performTextInput(sampleContactsData.name)
        composeTestRule.onNodeWithTag("inputSurname").performTextInput(sampleContactsData.surname)
        composeTestRule.onNodeWithTag("inputAge")
            .performTextInput(sampleContactsData.age.toString())
        composeTestRule.onNodeWithTag("inputPhone").performTextInput(sampleContactsData.phone)

        composeTestRule.onNodeWithText("Salvar contato").assertIsDisplayed()

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        saveContactFlow.value = State.Error(Throwable("Erro ao salvar contato"))

        composeTestRule.onNodeWithText("Ocorreu um erro ao tentar salvar o contato!")
            .assertIsDisplayed()
    }

    @Test
    fun testValidateFieldsWithEmptyInputs() {
        nameErrorFlow.value = false
        surnameErrorFlow.value = false
        ageErrorFlow.value = false
        phoneErrorFlow.value = false
        setContent()

        composeTestRule.onNodeWithText("Salvar").performClick()

        nameErrorFlow.value = true
        surnameErrorFlow.value = true
        ageErrorFlow.value = true
        phoneErrorFlow.value = true

        composeTestRule.onNodeWithTag("inputName").assertTextContains("Campo obrigatório")
        composeTestRule.onNodeWithTag("inputSurname").assertTextContains("Campo obrigatório")
        composeTestRule.onNodeWithTag("inputAge").assertTextContains("Campo obrigatório")
        composeTestRule.onNodeWithTag("inputPhone").assertTextContains("Campo obrigatório")
    }

    @Test
    fun testImagePickerDropdownMenuOptionClickAndShowDropdownMenu() {
        setContent()

        composeTestRule.onNodeWithTag("imgButton").performClick()

        composeTestRule.onNodeWithText("Foto com a câmera").assertIsDisplayed()
        composeTestRule.onNodeWithText("Foto da galeria").assertIsDisplayed()
    }
}