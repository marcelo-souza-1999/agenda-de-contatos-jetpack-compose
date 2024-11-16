package com.marcelos.agendadecontatos.presentation.ui.activity

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testShowContactsScreenIsDisplayed() {
        composeTestRule.setContent {
            ContactsAgendaApp()
        }

        composeTestRule.onNodeWithText("Agenda de Contatos")
            .assertIsDisplayed()
    }
}