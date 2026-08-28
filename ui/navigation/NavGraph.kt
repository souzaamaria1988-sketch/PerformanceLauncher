package com.seunome.perflauncher.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seunome.perflauncher.ui.screens.HomeScreen
import com.seunome.perflauncher.ui.screens.ProfilesScreen
import com.seunome.perflauncher.ui.screens.ModsScreen
import com.seunome.perflauncher.ui.screens.SettingsScreen

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Início")
    object Profiles : Screen("profiles", "Perfis")
    object Mods : Screen("mods", "Mods")
    object Settings : Screen("settings", "Config")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Profiles.route) { ProfilesScreen() }
        composable(Screen.Mods.route) { ModsScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
