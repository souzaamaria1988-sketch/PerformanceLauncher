package com.seunome.perflauncher.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seunome.perflauncher.ui.screens.HomeScreen
import com.seunome.perflauncher.ui.screens.ProfilesScreen
import com.seunome.perflauncher.ui.screens.ModsScreen
import com.seunome.perflauncher.ui.screens.ModDetailScreen
import com.seunome.perflauncher.ui.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen() }
        composable("profiles") { ProfilesScreen() }
        composable("mods") { 
            ModsScreen(
                onNavigateBack = { navController.popBackStack() },
                onModClick = { modId -> navController.navigate("mod_detail/$modId") }
            )
        }
        composable("mod_detail/{modId}") { backStackEntry ->
            val modId = backStackEntry.arguments?.getString("modId") ?: return@composable
            ModDetailScreen(
                modId = modId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("settings") { SettingsScreen() }
    }
}
