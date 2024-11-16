package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
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
class WarningDialogComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun warningDialog_displaysTitleAndMessage() {
        composeTestRule.setContent {
            WarningDialog(
                title = "Atenção",
                message = "Você precisa conceder permissão para continuar.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_permission_dialog,
                    negativeButtonText = R.string.txt_btn_negative_permission_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {},
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Atenção").assertExists()
        composeTestRule.onNodeWithText("Você precisa conceder permissão para continuar.")
            .assertExists()
    }

    @Test
    fun warningDialog_displaysTitleAndMessageAtCameraPermissionDenied() {
        val rationaleMessage = "câmera"

        composeTestRule.setContent {
            WarningDialog(
                title = stringResource(id = R.string.title_permission_denied_dialog),
                message = if (rationaleMessage.contains(
                        other = "câmera", ignoreCase = true
                    )
                ) {
                    "Como a permissão para usar a câmera foi negada.\nPara o app funcionar corretamente,\n" +
                            "você precisa ativar a permissão manualmente nas configurações do app."
                } else {
                    "Como a permissão para usar a galeria foi negada.\\nPara o app funcionar corretamente,\n" +
                            "você precisa ativar a permissão manualmente nas configurações do app."
                },
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_permission_denied_dialog,
                    negativeButtonText = R.string.txt_btn_negative_permission_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {},
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Permissão negada").assertExists()
        composeTestRule.onNodeWithText(
            "Como a permissão para usar a câmera foi negada.\nPara o app funcionar corretamente,\n" +
                    "você precisa ativar a permissão manualmente nas configurações do app."
        ).assertExists()
    }

    @Test
    fun warningDialog_displaysTitleAndMessageAtGalleryPermissionDenied() {
        val rationaleMessage = "galeria"

        composeTestRule.setContent {
            WarningDialog(
                title = stringResource(id = R.string.title_permission_denied_dialog),
                message = if (rationaleMessage.contains(
                        other = "câmera", ignoreCase = true
                    )
                ) {
                    "Como a permissão para usar a câmera foi negada.\nPara o app funcionar corretamente,\n" +
                            "você precisa ativar a permissão manualmente nas configurações do app."
                } else {
                    "Como a permissão para usar a galeria foi negada.\nPara o app funcionar corretamente,\n" +
                            "você precisa ativar a permissão manualmente nas configurações do app."
                },
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_permission_denied_dialog,
                    negativeButtonText = R.string.txt_btn_negative_permission_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {},
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Permissão negada").assertExists()
        composeTestRule.onNodeWithText(
            "Como a permissão para usar a galeria foi negada.\nPara o app funcionar corretamente,\n" +
                    "você precisa ativar a permissão manualmente nas configurações do app."
        ).assertExists()
    }

    @Test
    fun warningDialog_positiveButton_callsOnConfirmClick() {
        var isConfirmed = false

        composeTestRule.setContent {
            WarningDialog(
                title = "Atenção",
                message = "Você precisa conceder permissão para continuar.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_permission_dialog,
                    negativeButtonText = R.string.txt_btn_negative_permission_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {
                    isConfirmed = true
                },
                onDismissClick = {}
            )
        }

        composeTestRule.onNodeWithText("Conceder permissão").performClick()

        assertTrue(isConfirmed)
    }

    @Test
    fun warningDialog_negativeButton_callsOnDismissClick() {
        var isDismissed = false

        composeTestRule.setContent {
            WarningDialog(
                title = "Atenção",
                message = "Você precisa conceder permissão para continuar.",
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_permission_dialog,
                    negativeButtonText = R.string.txt_btn_negative_permission_dialog,
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