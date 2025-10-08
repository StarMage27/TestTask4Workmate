package io.github.starmage27.testtask4workmate.data

import android.util.Log
import kotlin.math.max

interface MyLoader<T> {
    val dao: AppDao
    val retrofitInstance: RetrofitInstance
    suspend fun loadFromDatabase(): List<T>
    suspend fun limitedLoadFromDatabase(amount: Int): List<T>
    suspend fun insertIntoDatabase(list: List<T>)
    suspend fun countEntries(): Int
    suspend fun getPagesRange(): IntRange
    suspend fun download(pages: IntRange = 1..1): List<T>

    suspend fun autoLoad(amount: Int? = null): List<T> {
        val entriesCount = countEntries()
        val autoloadedList = if (entriesCount > 0) {
            Log.i("MyLoader", "Loading from the database")
            if (amount == null) {
                val loadedList = loadFromDatabase()
                loadedList
            } else {
                val count = max(entriesCount, amount)
                val loadedList = limitedLoadFromDatabase(count)
                loadedList
            }
        } else {
            Log.i("MyLoader", "Downloading")
                val downloadedList = download(getPagesRange())
            insertIntoDatabase(downloadedList)
            downloadedList
        }

        return autoloadedList
    }
}