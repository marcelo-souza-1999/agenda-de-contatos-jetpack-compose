package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val buttonText = "Salvar Contato"
    private var wasButtonClicked = false

    @Before
    fun setUp() {
        wasButtonClicked = false
    }

    @Test
    fun primaryButton_displaysCorrectText_andTriggersClickAction() {
        composeTestRule.setContent {
            ContactsAgendaTheme {
                PrimaryButton(
                    onClickBtn = { wasButtonClicked = true },
                    text = buttonText,
                    modifier = Modifier.testTag("primaryButtonComponent"),
                    backgroundColor = Color.Blue
                )
            }
        }

        composeTestRule.onNodeWithTag("primaryButtonComponent")
            .assertIsDisplayed()
            .assertTextContains(buttonText)

        composeTestRule.onNodeWithText(buttonText).performClick()
    }
}