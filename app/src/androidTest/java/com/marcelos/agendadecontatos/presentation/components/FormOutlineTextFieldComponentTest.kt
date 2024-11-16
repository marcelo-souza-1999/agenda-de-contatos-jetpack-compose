package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormOutlineTextFieldComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun formOutlinedTextField_displaysInitialValue_andUpdatesOnTextInput() {
        var textFieldValue = "Initial Value"

        composeTestRule.setContent {
            FormOutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = "Label"
            )
        }

        composeTestRule.onNodeWithText("Initial Value").assertIsDisplayed()

        composeTestRule.onNodeWithText("Initial Value").performTextReplacement("New Value")

        assert(textFieldValue == "New Value")
    }

    @Test
    fun formOutlinedTextField_triggersOnValueChange_whenTextIsEntered() {
        var textFieldValue = ""
        val onValueChange: (String) -> Unit = { textFieldValue = it }

        composeTestRule.setContent {
            FormOutlinedTextField(
                value = textFieldValue,
                onValueChange = onValueChange,
                label = "Name"
            )
        }

        val inputText = "Novo Texto"
        composeTestRule.onNodeWithText("Name").performTextInput(inputText)
        assert(textFieldValue == inputText)
    }


    @Test
    fun formOutlinedTextField_showsErrorMessage_whenIsErrorIsTrue() {
        val errorMessage = "Campo obrigatório"

        composeTestRule.setContent {
            FormOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "Label",
                isError = true,
                errorMessage = errorMessage
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}