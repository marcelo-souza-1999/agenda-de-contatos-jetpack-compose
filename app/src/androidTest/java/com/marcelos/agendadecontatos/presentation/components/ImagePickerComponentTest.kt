package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImagePickerComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenImagePickerIsClicked_thenDropdownMenuIsDisplayed() {
        composeTestRule.setContent {
            ImagePicker(onImageSelected = {})
        }

        composeTestRule.onNodeWithTag("imgButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText("Foto com a câmera")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Foto da galeria")
            .assertIsDisplayed()
    }

    @Test
    fun whenGalleryOptionIsClicked_thenGalleryLauncherIsTriggered() {
        composeTestRule.setContent {
            ImagePicker(
                onImageSelected = { }
            )
        }

        composeTestRule.onNodeWithTag("imgButton").performClick()

        composeTestRule.onNodeWithText("Foto da galeria")
            .performClick()
    }

    @Test
    fun whenCameraOptionIsClicked_thenCameraLauncherIsTriggered() {
        composeTestRule.setContent {
            ImagePicker(
                onImageSelected = { }
            )
        }

        composeTestRule.onNodeWithTag("imgButton").performClick()

        composeTestRule.onNodeWithText("Foto com a câmera")
            .performClick()
    }
}