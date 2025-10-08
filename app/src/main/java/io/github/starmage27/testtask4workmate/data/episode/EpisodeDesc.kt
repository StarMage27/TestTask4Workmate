package io.github.starmage27.testtask4workmate.data.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Suppress("PropertyName")
@Entity(tableName = "episode_table")
data class EpisodeDesc(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "episode_id")
    val id: Int,

    @ColumnInfo(name = "air_date")
    val air_date: String,
    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "episode")
    val episode: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "characters")
    val characters: List<String>,
)