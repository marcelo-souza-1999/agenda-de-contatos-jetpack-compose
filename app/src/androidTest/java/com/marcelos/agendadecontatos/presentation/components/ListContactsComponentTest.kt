package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListContactsComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun listContactsItem_cardIsDisplayed() {
        composeTestRule.setContent {
            ListContactsItem(
                contactsData = ContactsViewData(
                    name = "Marcelo",
                    surname = "Souza",
                    age = 30,
                    phone = "12996259393",
                    imagePath = null
                ),
                navController = rememberNavController(),
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithTag("cardContact").assertExists()
    }

    @Test
    fun listContactsItem_displaysContactImage() {
        composeTestRule.setContent {
            ListContactsItem(
                contactsData = ContactsViewData(
                    id = 1,
                    name = "Marcelo",
                    surname = "Souza",
                    age = 30,
                    phone = "12996259393",
                    imagePath = "path/to/image.jpg"
                ),
                navController = rememberNavController(),
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithTag("imageContact").assertExists()
    }

    @Test
    fun listContactsItem_navigateToUpdateContact_whenEditIconClicked() {
        val contact = ContactsViewData(
            id = 1,
            name = "John",
            surname = "Doe",
            age = 30,
            phone = "12123456789",
            imagePath = null
        )

        var navigatedRoute: String? = null

        composeTestRule.setContent {
            val navController = rememberNavController().apply {
                addOnDestinationChangedListener { _, destination, _ ->
                    navigatedRoute = destination.route
                }
            }
            NavHost(navController = navController, startDestination = Routes.ShowContacts.route) {
                composable(Routes.ShowContacts.route) {
                    ListContactsItem(
                        contactsData = contact,
                        navController = navController,
                        onDelete = {}
                    )
                }
                composable("${Routes.UpdateContact.route}/${contact.id}") {}
            }
        }

        composeTestRule.onNodeWithTag("iconEditContact").performClick()

        assertEquals("${Routes.UpdateContact.route}/${contact.id}", navigatedRoute)
    }

    @Test
    fun listContactsItem_callsOnDelete_whenDeleteIconClicked() {
        var deleteCalled = false
        composeTestRule.setContent {
            ListContactsItem(
                contactsData = ContactsViewData(
                    id = 1,
                    name = "Marcelo",
                    surname = "Souza",
                    age = 30,
                    phone = "12996259393",
                    imagePath = null
                ),
                navController = rememberNavController(),
                onDelete = { deleteCalled = true }
            )
        }

        composeTestRule.onNodeWithTag("iconDeleteContact").performClick()
        assert(deleteCalled)
    }

}