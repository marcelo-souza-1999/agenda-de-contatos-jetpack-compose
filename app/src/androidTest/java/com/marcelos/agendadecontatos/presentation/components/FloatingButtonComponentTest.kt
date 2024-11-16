package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FloatingButtonComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun actionFloatingButton_rendersWithDefaultProperties() {
        composeTestRule.setContent {
            ActionFloatingButton(
                onClick = {},
                icon = Icons.Default.Add
            )
        }

        composeTestRule.onNodeWithTag("floatingActionBtn").assertExists()
    }

    @Test
    fun actionFloatingButton_callsOnClick_whenClicked() {
        val onClickMock = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            ActionFloatingButton(
                onClick = onClickMock,
                icon = Icons.Default.Add,
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        }

        composeTestRule.onNodeWithTag("floatingActionBtn").performClick()

        verify { onClickMock.invoke() }
    }
}