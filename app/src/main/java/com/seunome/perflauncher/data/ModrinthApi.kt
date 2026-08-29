package com.seunome.perflauncher.data

import com.seunome.perflauncher.domain.Mod
import com.seunome.perflauncher.domain.ModVersion
import com.seunome.perflauncher.domain.ModDetail
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private interface ModrinthService {
    @GET("v2/search")
    suspend fun search(
        @Query("query") query: String,
        @Query("facets") facets: String = "[[\"project_type:mod\"]]",
        @Query("limit") limit: Int = 20
    ): ModrinthResponse

    @GET("v2/project/{id}")
    suspend fun getProject(@Path("id") id: String): ModrinthProject

    @GET("v2/project/{id}/version")
    suspend fun getVersions(@Path("id") id: String): List<ModrinthVersion>
}

private data class ModrinthResponse(val hits: List<ModrinthHit>)
private data class ModrinthHit(
    val project_id: String,
    val title: String,
    val description: String,
    val downloads: Long,
    val slug: String,
    val icon_url: String?,
    val categories: List<String>,
    val project_type: String
)

private data class ModrinthProject(
    val id: String,
    val slug: String,
    val title: String,
    val description: String,
    val downloads: Long,
    val icon_url: String?,
    val body: String?,
    val categories: List<String>,
    val project_type: String
)

private data class ModrinthVersion(
    val id: String,
    val name: String,
    val version_number: String,
    val game_versions: List<String>,
    val loaders: List<String>,
    val files: List<ModrinthFile>,
    val date_published: String
)

private data class ModrinthFile(
    val url: String,
    val size: Long
)

object ModrinthApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.modrinth.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ModrinthService::class.java)

    suspend fun searchMods(query: String): List<Mod> {
        return try {
            retrofit.search(query).hits.map {
                Mod(
                    id = it.project_id,
                    title = it.title,
                    description = it.description,
                    downloads = it.downloads,
                    slug = it.slug,
                    iconUrl = it.icon_url,
                    categories = it.categories,
                    projectType = it.project_type
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getModDetail(modId: String): ModDetail? {
        return try {
            val project = retrofit.getProject(modId)
            val versions = retrofit.getVersions(modId)
            
            val mod = Mod(
                id = project.id,
                title = project.title,
                description = project.description,
                downloads = project.downloads,
                slug = project.slug,
                iconUrl = project.icon_url,
                categories = project.categories,
                projectType = project.project_type
            )
            
            val modVersions = versions.map { v ->
                ModVersion(
                    id = v.id,
                    name = v.name,
                    versionNumber = v.version_number,
                    gameVersions = v.game_versions,
                    modLoader = v.loaders.firstOrNull() ?: "unknown",
                    downloadUrl = v.files.firstOrNull()?.url ?: "",
                    fileSize = v.files.firstOrNull()?.size ?: 0L,
                    publishedDate = v.date_published
                )
            }
            
            ModDetail(
                mod = mod,
                versions = modVersions,
                fullDescription = project.body ?: "",
                imageUrl = project.icon_url
            )
        } catch (e: Exception) {
            null
        }
    }
}
