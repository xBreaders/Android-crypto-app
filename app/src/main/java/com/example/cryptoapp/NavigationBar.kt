package com.example.cryptoapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
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
import com.example.cryptoapp.ui.detailpage.CryptoDetailScreen
import com.example.cryptoapp.ui.mainpage.CryptoListScreen
import com.example.cryptoapp.ui.searchpage.SearchCryptoScreen


@Composable
fun MainScreen(navController: NavHostController) {
    val items = listOf(
        Screen.Main,
        Screen.Search,
    )

    Scaffold(
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
    data object Main : Screen("overview", Icons.Filled.Home, "Overview")
    data object Search : Screen("Search", Icons.Filled.Search, "Search")
}

