package com.seunome.perflauncher.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.seunome.perflauncher.ui.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Profiles, Screen.Mods, Screen.Settings)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        Screen.Home -> Icon(Icons.Default.Home, screen.label)
                        Screen.Profiles -> Icon(Icons.Default.Person, screen.label)
                        Screen.Mods -> Icon(Icons.Default.Extension, screen.label)
                        Screen.Settings -> Icon(Icons.Default.Settings, screen.label)
                    }
                },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}
