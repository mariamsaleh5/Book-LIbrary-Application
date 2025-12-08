package com.example.book_library_application

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.collections.forEach

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Library : NavigationItem("main", Icons.Default.Home, "Library")
    object AddBook : NavigationItem("add", Icons.Default.Add, "Add Book")
    object ReadBooks : NavigationItem("read", Icons.Default.CheckCircle, "Read Books")
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.Library,
        NavigationItem.AddBook,
        NavigationItem.ReadBooks
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
