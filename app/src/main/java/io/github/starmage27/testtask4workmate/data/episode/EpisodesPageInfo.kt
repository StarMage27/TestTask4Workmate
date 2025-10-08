package io.github.starmage27.testtask4workmate.data.episode

import io.github.starmage27.testtask4workmate.data.Info

data class EpisodesPageInfo(
    val info: Info,
    val results: List<EpisodeDesc>
)