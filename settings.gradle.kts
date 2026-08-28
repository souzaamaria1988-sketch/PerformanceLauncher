package com.seunome.perflauncher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Performance Launcher", style = MaterialTheme.typography.headlineLarge)
        Text("Seu launcher de Minecraft focado em FPS", 
             style = MaterialTheme.typography.bodyLarge)
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Status do Runtime", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("⚠️ Runtime Java não configurado")
                Text("Baixe o pacote de runtime nas configurações")
            }
        }
        
        Button(
            onClick = { /* TODO: Lançar Minecraft */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("JOGAR")
        }
    }
}
