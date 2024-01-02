package com.example.cryptoapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cryptoapp.persistence.api.SharedViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MainScreen(navController, SharedViewModel())
        }
    }

    @Test
    fun bottomNavigationFromMainToSearch() {

        composeTestRule
            .onNodeWithText("Search", substring = true)
            .performClick()


        composeTestRule.onNodeWithText("Search Crypto").assertIsDisplayed()
    }

    @Test
    fun bottomNavigationFromSearchToMain() {
        composeTestRule
            .onNodeWithText("Search", substring = true)
            .performClick()

        composeTestRule
            .onNodeWithText("Search Crypto")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Overview").performClick()


        composeTestRule.onNodeWithText("Price (USD)").assertIsDisplayed()
    }

    @Test
    fun bottomNavigationFromMainToDetail() {

        composeTestRule.onNodeWithText("Price (USD)", substring = true, ignoreCase = false)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("BTC")
            .performClick()

        composeTestRule.onNodeWithText("General Info").assertIsDisplayed()
    }


}