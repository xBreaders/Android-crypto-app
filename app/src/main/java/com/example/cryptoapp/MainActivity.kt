package com.example.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    MainScreen(
                        navController = navController,
                        sharedVM = (application as CoinApp).sharedVM
                    )
                }
            }
        }
    }
}
