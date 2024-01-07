package com.example.cryptoapp

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
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

/**
 * Test class for navigation within the Crypto App using Jetpack Compose and Android Navigation Component.
 *
 * This class includes tests for navigating between different screens in the application using the bottom navigation bar.
 * The tests ensure that the expected screens are displayed when navigation occurs.
 *
 * Annotations:
 * - [RunWith]: AndroidJUnit4 to enable Android framework testing with JUnit.
 * - [OptIn]: Marks the usage of an experimental API in Jetpack Compose for window size classes.
 *
 * @property composeTestRule JUnit rule for testing Compose UIs. Initializes the testing environment for Compose.
 */
@RunWith(AndroidJUnit4::class)
class NavigationTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Sets up the test environment before each test. This includes setting the Compose content to the main screen
     * of the application with a mock NavController and SharedViewModel.
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Before
    fun setUp() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            MainScreen(
                navController, SharedViewModel(), windowSizeClass = calculateWindowSizeClass(
                    (context as? Activity)!!
                )
            )
        }
    }

    /**
     * Tests navigation from the main screen to the search screen using the bottom navigation bar.
     * Verifies if the search screen is displayed after the navigation.
     */
    @Test
    fun bottomNavigationFromMainToSearch() {
        composeTestRule
            .onNodeWithText("Search", substring = true)
            .performClick()

        composeTestRule.onNodeWithText("Search Crypto").assertIsDisplayed()
    }

    /**
     * Tests navigation from the search screen back to the main screen using the bottom navigation bar.
     * Verifies if the main screen is displayed after the navigation.
     */
    @Test
    fun bottomNavigationFromSearchToMain() {
        composeTestRule
            .onNodeWithText("Search", substring = true)
            .performClick()

        composeTestRule.onNodeWithText("Search Crypto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Overview").performClick()

        composeTestRule.onNodeWithText("Price (USD)").assertIsDisplayed()
    }

    /**
     * Tests navigation from the main screen to a detail screen by selecting a crypto item.
     * Verifies if the details screen for the selected crypto is displayed after the navigation.
     */
    @Test
    fun bottomNavigationFromMainToDetail() {
        composeTestRule.onNodeWithText("Price (USD)", substring = true, ignoreCase = false)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("BTC").performClick()

        composeTestRule.onNodeWithText("General Info").assertIsDisplayed()
    }
}