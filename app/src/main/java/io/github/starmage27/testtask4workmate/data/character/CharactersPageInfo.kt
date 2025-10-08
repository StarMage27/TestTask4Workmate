package io.github.starmage27.testtask4workmate.data.character

import io.github.starmage27.testtask4workmate.data.Info

data class CharactersPageInfo(
    val info: Info,
    val results: List<CharacterDesc>
)