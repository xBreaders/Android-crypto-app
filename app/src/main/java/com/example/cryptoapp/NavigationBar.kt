package com.example.cryptoapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun MainScreen(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Market,
        Screen.Profile
    )

    val MainCryptoVM = MainCryptoViewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(items, navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { CryptoListScreen(MainCryptoVM) }
            composable(Screen.Market.route) { /* Market Screen Content */ }
            composable(Screen.Profile.route) { /* Profile Screen Content */ }
            composable("cryptoDetail/{cryptoName}") { backStackEntry ->
                CryptoDetailScreen(
                    navController,
                    cryptoName = backStackEntry.arguments?.getString("cryptoName") ?: "Unknown")
            }
            // Add composable for other screens if necessary
        }
    }
}

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

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Filled.Home, "Home")
    object Market : Screen("market", Icons.Filled.List, "Market")
    object Profile : Screen("profile", Icons.Filled.Person, "Profile")
}

