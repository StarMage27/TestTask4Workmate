package io.github.starmage27.testtask4workmate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import io.github.starmage27.testtask4workmate.data.character.CharacterDesc
import io.github.starmage27.testtask4workmate.data.episode.EpisodeDesc
import io.github.starmage27.testtask4workmate.data.location.LocationDesc
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // region Character
    @Upsert
    suspend fun insertCharacters(characters: List<CharacterDesc>)
    @Upsert
    suspend fun insertCharacter(characterDesc: CharacterDesc)
    @Delete
    suspend fun deleteCharacter(characterDesc: CharacterDesc)
    @Query("SELECT * FROM character_table WHERE character_id = :id")
    suspend fun findCharacter(id: String): List<CharacterDesc>
    @Query("SELECT * FROM character_table")
    fun getAllCharactersFlow(): Flow<List<CharacterDesc>>
    @Query("SELECT * FROM character_table")
    suspend fun getAllCharacters(): List<CharacterDesc>
    @Query("SELECT * FROM character_table LIMIT :amount")
    suspend fun getLimitedCharacters(amount: Int): List<CharacterDesc>
    @Query("SELECT count(*) FROM character_table")
    suspend fun countCharacters(): Int
    @Query("SELECT * FROM character_table WHERE " +
            "name    LIKE '%' || :name    || '%' AND " +
            "status  LIKE '%' || :status  || '%' AND " +
            "species LIKE '%' || :species || '%' AND " +
            "type    LIKE '%' || :type    || '%' AND " +
            "gender  LIKE '%' || :gender  || '%'")
    suspend fun filterCharacters(
        name    : String = "",
        status  : String = "",
        species : String = "",
        type    : String = "",
        gender  : String = "",
    ): List<CharacterDesc>
    @Query("SELECT DISTINCT status FROM character_table")
    suspend fun getUniqueStatuses(): List<String>
    @Query("SELECT DISTINCT species FROM character_table")
    suspend fun getUniqueSpecies(): List<String>
    @Query("SELECT DISTINCT type FROM character_table")
    suspend fun getUniqueTypes(): List<String>
    @Query("SELECT DISTINCT gender FROM character_table")
    suspend fun getUniqueGenders(): List<String>
    // endregion

    // region Location
    @Upsert
    fun insertLocations(locations: List<LocationDesc>)
    @Upsert
    suspend fun insertLocation(locationDesc: LocationDesc)
    @Delete
    suspend fun deleteLocation(locationDesc: LocationDesc)
    @Query("SELECT * FROM location_table WHERE location_id = :id")
    suspend fun findLocation(id: String): List<LocationDesc>
    @Query("SELECT * FROM location_table")
    fun getAllLocationsFlow(): Flow<List<LocationDesc>>
    @Query("SELECT * FROM location_table")
    suspend fun getAllLocations(): List<LocationDesc>
    @Query("SELECT * FROM location_table LIMIT :amount")
    suspend fun getLimitedLocations(amount: Int): List<LocationDesc>
    @Query("SELECT count(*) FROM location_table")
    suspend fun countLocations(): Int
    // endregion

    // region Episode
    @Upsert
    suspend fun insertEpisodes(episodes: List<EpisodeDesc>)
    @Upsert
    suspend fun insertEpisode(episodeDesc: EpisodeDesc)
    @Delete
    suspend fun deleteEpisode(episodeDesc: EpisodeDesc)
    @Query("SELECT * FROM episode_table WHERE episode_id = :id")
    suspend fun findEpisode(id: String): List<EpisodeDesc>
    @Query("SELECT * FROM episode_table")
    fun getAllEpisodesFlow(): Flow<List<EpisodeDesc>>
    @Query("SELECT * FROM episode_table")
    suspend fun getAllEpisodes(): List<EpisodeDesc>
    @Query("SELECT * FROM episode_table LIMIT :amount")
    suspend fun getLimitedEpisodes(amount: Int): List<EpisodeDesc>
    @Query("SELECT count(*) FROM episode_table")
    suspend fun countEpisodes(): Int
    // endregion
}
