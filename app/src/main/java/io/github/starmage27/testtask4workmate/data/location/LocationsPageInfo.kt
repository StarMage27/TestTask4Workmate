package io.github.starmage27.testtask4workmate.data.location

import io.github.starmage27.testtask4workmate.data.Info

data class LocationsPageInfo(
    val info: Info,
    val results: List<LocationDesc>
)