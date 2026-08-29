package com.seunome.perflauncher.data

import com.seunome.perflauncher.domain.Mod
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private interface ModrinthService {
    @GET("v2/search")
    suspend fun search(
        @Query("query") query: String,
        @Query("facets") facets: String = "[[\"project_type:mod\"]]",
        @Query("limit") limit: Int = 20
    ): ModrinthResponse
}

private data class ModrinthResponse(val hits: List<ModrinthHit>)

private data class ModrinthHit(
    val project_id: String,
    val title: String,
    val description: String,
    val downloads: Long,
    val slug: String
)

object ModrinthApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.modrinth.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ModrinthService::class.java)

    suspend fun searchMods(query: String): List<Mod> {
        val response = retrofit.search(query)
        return response.hits.map {
            Mod(
                id = it.project_id,
                title = it.title,
                description = it.description,
                downloads = it.downloads,
                slug = it.slug
            )
        }
    }
}
