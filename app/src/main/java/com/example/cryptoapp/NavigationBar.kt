package com.example.cryptoapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cryptoapp.persistence.api.SharedViewModel
import com.example.cryptoapp.ui.detailpage.CryptoDetailScreen
import com.example.cryptoapp.ui.mainpage.CryptoListScreen
import com.example.cryptoapp.ui.searchpage.SearchCryptoScreen

/**
 * Composable function to set up the main screen of the Crypto App with a bottom navigation bar.
 *
 * All top-level destinations are supposed to be added here.
 *
 * @param navController The NavController which controls the navigation within Compose.
 * @param sharedVM The SharedViewModel which holds shared data across multiple composables.
 */
@Composable
fun MainScreen(navController: NavHostController, sharedVM: SharedViewModel) {
    val items = listOf(
        Screen.Main,
        Screen.Search,
    )
    val errorMessage by sharedVM.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                })
        },
        bottomBar = { BottomNavigationBar(items, navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Main.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Main.route) { CryptoListScreen(navController) }
            composable(Screen.Search.route) { SearchCryptoScreen(navController) }
            composable("cryptoDetail/{cryptoId}") { backStackEntry ->
                val cryptoIdStr = backStackEntry.arguments?.getString("cryptoId") ?: "1"
                val cryptoId = cryptoIdStr.toIntOrNull() ?: 1

                CryptoDetailScreen(
                    navController,
                    cryptoId = cryptoId,
                )
            }
        }
        errorMessage?.let { message ->
            LaunchedEffect(key1 = message) {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
                sharedVM.clearError()
            }
        }
    }
}

/**
 * Composable function to set up the bottom navigation bar.
 *
 * All top-level destinations that needs to be included in bottom navigation should be passed as `items`.
 *
 * @param items A list of items representing the top-level destinations.
 * @param navController The NavController which controls the navigation within Composable.
 */
@Composable
fun BottomNavigationBar(items: List<Screen>, navController: NavHostController) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

/**
 * Sealed class to represent a screen.
 * Each object represents a top-level screen in our navigation hierarchy.
 *
 * @property route The unique route for a screen. It used as destination path in Composable methods.
 * @property icon The icon to represent the screen in components like Bottom Navigation, Drawer, etc.
 * @property label The label that describes this screen.
 */
sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    data object Main : Screen("overview", Icons.Filled.Home, "Overview")
    data object Search : Screen("Search", Icons.Filled.Search, "Search")
}

