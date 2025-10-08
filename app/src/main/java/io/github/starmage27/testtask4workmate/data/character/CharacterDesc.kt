package io.github.starmage27.testtask4workmate.data.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class CharacterDesc(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "character_id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "episodes")
    val episode: List<String>,

    @ColumnInfo(name = "location")
    val location: CharacterLocation,
    @ColumnInfo(name = "location_origin")
    val origin: CharacterLocation,
)