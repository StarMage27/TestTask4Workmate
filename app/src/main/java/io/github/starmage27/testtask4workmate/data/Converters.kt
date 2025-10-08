package io.github.starmage27.testtask4workmate.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.starmage27.testtask4workmate.data.character.CharacterLocation

class Converters {
    @TypeConverter
    fun jsonToListOfStrings(value: String): List<String> {
        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun listOfStringsToJson(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToLocationInCharacter(value: String): CharacterLocation {
        return Gson().fromJson(value, object : TypeToken<CharacterLocation>() {}.type)
    }

    @TypeConverter
    fun locationInCharacterToJson(locationInCharacter: CharacterLocation): String {
        return Gson().toJson(locationInCharacter)
    }
}