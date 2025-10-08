package io.github.starmage27.testtask4workmate.data.location

import io.github.starmage27.testtask4workmate.data.AppDao
import io.github.starmage27.testtask4workmate.data.MyLoader
import io.github.starmage27.testtask4workmate.data.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class LocationLoader(
    override val dao: AppDao,
    override val retrofitInstance: RetrofitInstance
) : MyLoader<LocationDesc> {
    override suspend fun loadFromDatabase(): List<LocationDesc> {
        return dao.getAllLocations()
    }

    override suspend fun limitedLoadFromDatabase(amount: Int): List<LocationDesc> {
        return dao.getLimitedLocations(amount)
    }

    override suspend fun insertIntoDatabase(list: List<LocationDesc>) {
        dao.insertLocations(list)
    }

    override suspend fun countEntries(): Int {
        return dao.countLocations()
    }

    override suspend fun getPagesRange(): IntRange {
        val response = retrofitInstance.api.getLocationsPage()
        val pages = response.body()?.info?.pages
        val pagesRange = 1..(pages?: 1)
        return pagesRange
    }

    override suspend fun download(pages: IntRange): List<LocationDesc> {
        val locations = coroutineScope {
            pages.map { page ->
                async(Dispatchers.IO) {
                    val response = retrofitInstance.api.getLocationsPage(page)
                    response.body()?.results.orEmpty()
                }
            }.awaitAll().flatten()
        }
        return locations
    }
}