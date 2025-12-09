package com.example.book_library_application

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import android.content.Intent
import android.net.Uri

class EspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        BookDatabase.getDatabase(context).clearAllTables()
        
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun verifyBooksListScreen() {

        composeTestRule.onNodeWithText("Book Library").assertIsDisplayed()


        composeTestRule.onNodeWithText("Add Book").performClick()
        composeTestRule.onNodeWithText("Book Name").assertIsDisplayed() // Confirm we are on Add Book Screen
    }

    @Test
    fun verifyAddBookScreenAndNavigation() {
        val bookName = "Test Book"
        val bookUrl = "www.goodreads.com/test"


        composeTestRule.onNodeWithText("Add Book").performClick()


        composeTestRule.onNodeWithText("Book Name").performTextInput(bookName)
        composeTestRule.onNodeWithText("Goodreads URL").performTextInput(bookUrl)


        composeTestRule.onNodeWithText("Save Book").performClick()


        composeTestRule.onNodeWithText("Book Library").assertIsDisplayed()
        composeTestRule.onNodeWithText(bookName).assertIsDisplayed()
    }

    @Test
    fun verifyReadBooksScreen() {
        val bookName = "Read Book Test"
        val bookUrl = "www.goodreads.com/read"


        composeTestRule.onNodeWithText("Add Book").performClick()
        composeTestRule.onNodeWithText("Book Name").performTextInput(bookName)
        composeTestRule.onNodeWithText("Goodreads URL").performTextInput(bookUrl)
        composeTestRule.onNodeWithText("Save Book").performClick()


        composeTestRule.onNode(isToggleable()).performClick()


        composeTestRule.onNodeWithText("Read Books").performClick()


        composeTestRule.onNodeWithTag("ReadBooksTitle").assertIsDisplayed() // Title
        composeTestRule.onNodeWithText(bookName).assertIsDisplayed()
        composeTestRule.onNodeWithText("✓ Read").assertIsDisplayed()
    }

    @Test
    fun verifyBookLinks() {
       val bookName = "Link Book"
       val bookUrl = "www.goodreads.com/link"


       composeTestRule.onNodeWithText("Add Book").performClick()
       composeTestRule.onNodeWithText("Book Name").performTextInput(bookName)
       composeTestRule.onNodeWithText("Goodreads URL").performTextInput(bookUrl)
       composeTestRule.onNodeWithText("Save Book").performClick()


       composeTestRule.onNodeWithText(bookName).performClick()


       val expectedUrl = "https://$bookUrl"
       intended(allOf(
           hasAction(Intent.ACTION_VIEW),
           hasData(Uri.parse(expectedUrl))
       ))
    }
}
