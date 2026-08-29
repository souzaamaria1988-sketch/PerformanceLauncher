package com.seunome.perflauncher.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.seunome.perflauncher.data.ModrinthApi
import com.seunome.perflauncher.domain.Mod
import com.seunome.perflauncher.domain.ModDetail
import com.seunome.perflauncher.domain.ModVersion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModsScreen(
    onNavigateBack: () -> Unit = {},
    onModClick: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var mods by remember { mutableStateOf<List<Mod>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
            }
            
            Text("Mods", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(Modifier.width(48.dp))
        }
        
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar mod no Modrinth") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    mods = ModrinthApi.searchMods(query)
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { 
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
            }
            Text("Buscar") 
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp)) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            } else {
                items(mods) { mod ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onModClick(mod.id) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            if (mod.iconUrl != null) {
                                AsyncImage(
                                    model = mod.iconUrl,
                                    contentDescription = mod.title,
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(12.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(mod.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    mod.description, 
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    mod.categories.take(3).forEach { category ->
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                category,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "${mod.downloads} downloads", 
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModDetailScreen(
    modId: String,
    onNavigateBack: () -> Unit = {}
) {
    var modDetail by remember { mutableStateOf<ModDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedVersion by remember { mutableStateOf<ModVersion?>(null) }
    var showDownloadConfirm by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(modId) {
        isLoading = true
        modDetail = ModrinthApi.getModDetail(modId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedVersion != null) "Selecionar Versão" else "Detalhes do Mod") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (modDetail == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Erro ao carregar mod")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        scope.launch {
                            isLoading = true
                            modDetail = ModrinthApi.getModDetail(modId)
                            isLoading = false
                        }
                    }) {
                        Text("Tentar Novamente")
                    }
                }
            } else {
                val detail = modDetail!!
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Header com imagem e informações
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                if (detail.imageUrl != null) {
                                    AsyncImage(
                                        model = detail.imageUrl,
                                        contentDescription = detail.mod.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        detail.mod.title,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        detail.mod.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(
                                                "Downloads",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                "${detail.mod.downloads}",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                        Column {
                                            Text(
                                                "Versões",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                "${detail.versions.size}",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(12.dp))
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        detail.mod.categories.forEach { category ->
                                            Surface(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = MaterialTheme.shapes.small
                                            ) {
                                                Text(
                                                    category,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        // Descrição completa
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Descrição",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    detail.fullDescription.replace(Regex("\\[.*?\\]\\(.*?\\)"), "")
                                        .replace("#", "")
                                        .trim(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        // Lista de versões
                        Text(
                            "Versões Disponíveis",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    
                    // Lista de versões
                    items(detail.versions) { version ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clickable {
                                    selectedVersion = version
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedVersion?.id == version.id)
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        version.name,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        "v${version.versionNumber}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        version.gameVersions.take(4).forEach { gameVersion ->
                                            Surface(
                                                color = MaterialTheme.colorScheme.secondaryContainer,
                                                shape = MaterialTheme.shapes.extraSmall
                                            ) {
                                                Text(
                                                    gameVersion,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        if (version.gameVersions.size > 4) {
                                            Text(
                                                "+${version.gameVersions.size - 4}",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Surface(
                                        color = when(version.modLoader.lowercase()) {
                                            "fabric" -> MaterialTheme.colorScheme.tertiaryContainer
                                            "forge" -> MaterialTheme.colorScheme.errorContainer
                                            "quilt" -> MaterialTheme.colorScheme.primaryContainer
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            version.modLoader.uppercase(),
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "%.2f MB".format(version.fileSize / 1024.0 / 1024.0),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Icon(
                                        Icons.Default.Download,
                                        contentDescription = "Download",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                    
                    item {
                        Spacer(Modifier.height(80.dp))
                    }
                }
                
                // Botão flutuante de download quando versão selecionada
                if (selectedVersion != null && !showDownloadConfirm) {
                    FloatingActionButton(
                        onClick = { showDownloadConfirm = true },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Text("Baixar")
                        }
                    }
                }
            }
        }
    }
    
    // Diálogo de confirmação de download
    if (showDownloadConfirm && selectedVersion != null) {
        AlertDialog(
            onDismissRequest = { showDownloadConfirm = false },
            title = { Text("Confirmar Download") },
            text = {
                Column {
                    Text("Você está prestes a baixar:")
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Mod: ${modDetail?.mod?.title ?: ""}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Versão: ${selectedVersion!!.name} (${selectedVersion!!.versionNumber})",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Mod Loader: ${selectedVersion!!.modLoader}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Versões do Minecraft: ${selectedVersion!!.gameVersions.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Tamanho: %.2f MB".format(selectedVersion!!.fileSize / 1024.0 / 1024.0),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO: Implementar download real do mod
                        showDownloadConfirm = false
                        selectedVersion = null
                    }
                ) {
                    Text("BAIXAR AGORA")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDownloadConfirm = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}
