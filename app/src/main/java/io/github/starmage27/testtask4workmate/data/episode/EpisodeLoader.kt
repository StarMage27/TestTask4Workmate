package io.github.starmage27.testtask4workmate.data.episode

import io.github.starmage27.testtask4workmate.data.AppDao
import io.github.starmage27.testtask4workmate.data.MyLoader
import io.github.starmage27.testtask4workmate.data.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class EpisodeLoader(
    override val dao: AppDao,
    override val retrofitInstance: RetrofitInstance
) : MyLoader<EpisodeDesc> {
    override suspend fun loadFromDatabase(): List<EpisodeDesc> {
        return dao.getAllEpisodes()
    }

    override suspend fun limitedLoadFromDatabase(amount: Int): List<EpisodeDesc> {
        return dao.getLimitedEpisodes(amount)
    }

    override suspend fun insertIntoDatabase(list: List<EpisodeDesc>) {
        dao.insertEpisodes(list)
    }

    override suspend fun countEntries(): Int {
        return dao.countEpisodes()
    }

    override suspend fun getPagesRange(): IntRange {
        val response = retrofitInstance.api.getEpisodesPage()
        val pages = response.body()?.info?.pages
        val pagesRange = 1..(pages?: 1)
        return pagesRange
    }

    override suspend fun download(pages: IntRange): List<EpisodeDesc> {
        val episodes = coroutineScope {
            pages.map { page ->
                async(Dispatchers.IO) {
                    val response = retrofitInstance.api.getEpisodesPage(page)
                    response.body()?.results.orEmpty()
                }
            }.awaitAll().flatten()
        }
        return episodes
    }
}