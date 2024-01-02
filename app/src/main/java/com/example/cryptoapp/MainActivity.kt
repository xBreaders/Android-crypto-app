package com.example.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import com.example.cryptoapp.ui.theme.CryptoAppTheme
/**
 * Main activity for the Crypto App.
 *
 * This activity serves as the host for composables and coordinate navigation between screens.
 * The composable structure of the app is defined in this activity, and this activity sets up the app theme and navigation controller.
 *
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val windowSizeClass = calculateWindowSizeClass(this)
                    MainScreen(
                        navController = navController,
                        sharedVM = (application as CoinApp).sharedVM,
                        windowSizeClass = windowSizeClass
                    )
                }
            }
        }
    }
}
