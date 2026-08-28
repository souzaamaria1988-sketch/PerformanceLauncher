package com.seunome.perflauncher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seunome.perflauncher.data.ModrinthApi
import com.seunome.perflauncher.domain.Mod
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModsScreen() {
    var query by remember { mutableStateOf("") }
    var mods by remember { mutableStateOf<List<Mod>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar mod no Modrinth") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            scope.launch {
                mods = ModrinthApi.searchMods(query)
            }
        }) { Text("Buscar") }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(mods) { mod ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(mod.title, style = MaterialTheme.typography.titleMedium)
                        Text(mod.description, style = MaterialTheme.typography.bodySmall)
                        Text("${mod.downloads} downloads", 
                             style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
