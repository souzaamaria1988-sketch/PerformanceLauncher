package com.seunome.perflauncher

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.seunome.perflauncher.ui.components.BottomNavBar
import com.seunome.perflauncher.ui.navigation.NavGraph

@Composable
fun PerformanceApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
