package io.github.starmage27.testtask4workmate.data.character

import io.github.starmage27.testtask4workmate.data.AppDao
import io.github.starmage27.testtask4workmate.data.MyLoader
import io.github.starmage27.testtask4workmate.data.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.collections.orEmpty

class CharacterLoader(
    override val dao: AppDao,
    override val retrofitInstance: RetrofitInstance
) : MyLoader<CharacterDesc> {
    override suspend fun loadFromDatabase(): List<CharacterDesc> {
        return dao.getAllCharacters()
    }

    override suspend fun limitedLoadFromDatabase(amount: Int): List<CharacterDesc> {
        return dao.getLimitedCharacters(amount)
    }

    override suspend fun insertIntoDatabase(list: List<CharacterDesc>) {
        dao.insertCharacters(list)
    }

    override suspend fun countEntries(): Int {
        return dao.countCharacters()
    }

    override suspend fun getPagesRange(): IntRange {
        val response = retrofitInstance.api.getCharactersPage()
        val pages = response.body()?.info?.pages
        val pagesRange = 1..(pages?: 1)
        return pagesRange
    }

    override suspend fun download(pages: IntRange): List<CharacterDesc> {
        val characters = coroutineScope {
            pages.map { page ->
                async(Dispatchers.IO) {
                    val response = retrofitInstance.api.getCharactersPage(page)
                    response.body()?.results.orEmpty()
                }
            }.awaitAll().flatten()
        }
        return characters
    }
}