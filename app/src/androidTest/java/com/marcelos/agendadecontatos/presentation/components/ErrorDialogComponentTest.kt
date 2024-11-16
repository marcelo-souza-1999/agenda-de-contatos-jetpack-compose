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
class ErrorDialogComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorDialog_displaysTitleAndMessage() {
        composeTestRule.setContent {
            ErrorDialog(
                title = "Erro ao salvar",
                message = "Ocorreu um erro ao tentar salvar o contato.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_error_dialog,
                    negativeButtonText = R.string.txt_btn_negative_error_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {},
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Erro ao salvar").assertExists()
        composeTestRule.onNodeWithText("Ocorreu um erro ao tentar salvar o contato.").assertExists()
    }

    @Test
    fun errorDialog_positiveButton_callsOnConfirmClick() {
        var isConfirmed = false

        composeTestRule.setContent {
            ErrorDialog(
                title = "Erro ao salvar",
                message = "Ocorreu um erro ao tentar salvar o contato.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_error_dialog,
                    negativeButtonText = R.string.txt_btn_negative_error_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {
                    isConfirmed = true
                },
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        assertTrue(isConfirmed)
    }

    @Test
    fun errorDialog_negativeButton_callsOnDismissClick() {
        var isDismissed = false

        composeTestRule.setContent {
            ErrorDialog(
                title = "Erro ao salvar",
                message = "Ocorreu um erro ao tentar salvar o contato.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_error_dialog,
                    negativeButtonText = R.string.txt_btn_negative_error_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {},
                onDismissClick = {
                    isDismissed = true
                }
            )
        }

        composeTestRule.onNodeWithText("Cancelar").performClick()

        assertTrue(isDismissed)
    }
}