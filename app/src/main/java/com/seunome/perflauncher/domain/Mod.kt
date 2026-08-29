package com.seunome.perflauncher.domain

data class Mod(
    val id: String,
    val title: String,
    val description: String,
    val downloads: Long,
    val slug: String,
    val iconUrl: String? = null,
    val categories: List<String> = emptyList(),
    val projectType: String = "mod"
)

data class ModVersion(
    val id: String,
    val name: String,
    val versionNumber: String,
    val gameVersions: List<String>,
    val modLoader: String,
    val downloadUrl: String,
    val fileSize: Long,
    val publishedDate: String
)

data class ModDetail(
    val mod: Mod,
    val versions: List<ModVersion>,
    val fullDescription: String,
    val imageUrl: String?
)
