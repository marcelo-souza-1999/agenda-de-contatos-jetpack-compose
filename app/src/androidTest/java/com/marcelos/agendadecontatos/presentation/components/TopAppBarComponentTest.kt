package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopAppBarComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topAppBar_displaysTitleCorrectly() {
        val titleText = "Agenda de Contatos"

        composeTestRule.setContent {
            ContactsAgendaTheme {
                TopAppBar(title = titleText)
            }
        }

        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
    }
}