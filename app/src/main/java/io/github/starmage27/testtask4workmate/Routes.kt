package io.github.starmage27.testtask4workmate

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class CharacterRoute(
    val id: Int
)
@Serializable
data class LocationRoute(
    val id: Int
)
@Serializable
data class EpisodeRoute(
    val id: Int
)

@Serializable
object AppInfoRoute