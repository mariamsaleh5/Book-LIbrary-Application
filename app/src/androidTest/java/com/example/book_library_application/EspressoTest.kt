package com.example.book_library_application

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class EspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainScreen_displaysTitle() {
        // Start the app and verify "Book Library" is displayed
        composeTestRule.onNodeWithText("Book Library").assertIsDisplayed()
    }
}
