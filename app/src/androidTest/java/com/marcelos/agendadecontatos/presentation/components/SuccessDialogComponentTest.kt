package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.White
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SuccessDialogComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun successDialog_displaysTitleAndMessage() {
        composeTestRule.setContent {
            SuccessDialog(
                title = "Success",
                message = "Your contact was saved successfully.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    neutralButtonText = R.string.txt_btn_neutral_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {},
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Success").assertExists()
        composeTestRule.onNodeWithText("Your contact was saved successfully.").assertExists()
        composeTestRule.onNodeWithText("OK").assertExists()
        composeTestRule.onNodeWithText("OK").performClick()
    }

    @Test
    fun successDialog_buttonClickTriggersActions() {
        var confirmClicked = false
        var dismissClicked = false

        composeTestRule.setContent {
            SuccessDialog(
                title = "Success",
                message = "Your contact was saved successfully.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    neutralButtonText = R.string.txt_btn_neutral_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {
                    confirmClicked = true
                },
                onDismissClick = {
                    dismissClicked = true
                }
            )
        }

        composeTestRule.onNodeWithText("OK").performClick()

        assertTrue(confirmClicked)
        assertTrue(dismissClicked)
    }
}