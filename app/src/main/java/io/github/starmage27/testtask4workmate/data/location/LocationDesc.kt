package io.github.starmage27.testtask4workmate.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationDesc(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "location_id")
    val id: Int,

    @ColumnInfo(name = "created")
    val created: String,
    @ColumnInfo(name = "dimension")
    val dimension: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "residents")
    val residents: List<String>,
)