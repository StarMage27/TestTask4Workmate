package io.github.starmage27.testtask4workmate.data

import io.github.starmage27.testtask4workmate.data.character.CharactersPageInfo
import io.github.starmage27.testtask4workmate.data.episode.EpisodesPageInfo
import io.github.starmage27.testtask4workmate.data.location.LocationsPageInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {
    @GET("character/")
    suspend fun getCharactersPage(@Query("page") page: Int = 1): Response<CharactersPageInfo>
    @GET("location/")
    suspend fun getLocationsPage(@Query("page") page: Int = 1): Response<LocationsPageInfo>
    @GET("episode/")
    suspend fun getEpisodesPage(@Query("page") page: Int = 1): Response<EpisodesPageInfo>
}