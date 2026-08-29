package com.seunome.perflauncher.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val items = listOf(
        "home" to Pair(Icons.Default.Home, "Início"),
        "profiles" to Pair(Icons.Default.Person, "Perfis"),
        "mods" to Pair(Icons.Default.Extension, "Mods"),
        "settings" to Pair(Icons.Default.Settings, "Config")
    )
    
    NavigationBar {
        items.forEach { (route, iconLabel) ->
            NavigationBarItem(
                icon = { Icon(iconLabel.first, contentDescription = iconLabel.second) },
                label = { Text(iconLabel.second) },
                selected = currentRoute == route,
                onClick = { navController.navigate(route) }
            )
        }
    }
}
