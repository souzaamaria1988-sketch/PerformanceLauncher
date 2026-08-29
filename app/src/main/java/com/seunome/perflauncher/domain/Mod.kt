package com.seunome.perflauncher.domain

data class Mod(
    val id: String,
    val title: String,
    val description: String,
    val downloads: Long,
    val slug: String
)
