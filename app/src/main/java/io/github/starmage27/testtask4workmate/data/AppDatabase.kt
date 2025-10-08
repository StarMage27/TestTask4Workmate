package io.github.starmage27.testtask4workmate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.starmage27.testtask4workmate.data.character.CharacterDesc
import io.github.starmage27.testtask4workmate.data.episode.EpisodeDesc
import io.github.starmage27.testtask4workmate.data.location.LocationDesc

@Database(entities = [(CharacterDesc::class), (LocationDesc::class), (EpisodeDesc::class)], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).fallbackToDestructiveMigration(true)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}