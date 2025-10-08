package io.github.starmage27.testtask4workmate

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.starmage27.testtask4workmate.data.AppDao
import io.github.starmage27.testtask4workmate.data.AppDatabase
import io.github.starmage27.testtask4workmate.data.RetrofitInstance
import io.github.starmage27.testtask4workmate.data.character.CharacterDesc
import io.github.starmage27.testtask4workmate.data.character.CharacterLoader
import io.github.starmage27.testtask4workmate.data.episode.EpisodeDesc
import io.github.starmage27.testtask4workmate.data.episode.EpisodeLoader
import io.github.starmage27.testtask4workmate.data.location.LocationDesc
import io.github.starmage27.testtask4workmate.data.location.LocationLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(val application: Application) : ViewModel()  {
    val anyString = application.getString(R.string.any)
    private val appDb = AppDatabase.Companion.getInstance(application)
    private val dao: AppDao = appDb.appDao()
    private val retrofitInstance: RetrofitInstance = RetrofitInstance
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val characterLoader = CharacterLoader(dao, retrofitInstance)
    private val locationLoader = LocationLoader(dao, retrofitInstance)
    private val episodeLoader = EpisodeLoader(dao, retrofitInstance)

    private val imageLoader = ImageLoader.Builder(application)
        //.logger(DebugLogger())
        .build()
    fun getImageLoader(): ImageLoader {
        return this.imageLoader
    }

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    private val _characters: MutableStateFlow<List<CharacterDesc>> = MutableStateFlow(emptyList())
    val characters = _characters.asStateFlow()
    private val _filteredCharacters: MutableStateFlow<List<CharacterDesc>> =
        MutableStateFlow(emptyList())
    val filteredCharacters = _filteredCharacters.asStateFlow()
    private val _filterMessage: MutableStateFlow<String> =
        MutableStateFlow(application.getString(R.string.enter_your_filters_and_press_search))
    val filterMessage = _filterMessage.asStateFlow()
    private val _locations: MutableStateFlow<List<LocationDesc>> = MutableStateFlow(emptyList())
    val locations = _locations.asStateFlow()
    private val _episodes: MutableStateFlow<List<EpisodeDesc>> = MutableStateFlow(emptyList())
    val episodes = _episodes.asStateFlow()

    private val _statusValues: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val statusValues = _statusValues.asStateFlow()
    private val _speciesValues: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val speciesValues = _speciesValues.asStateFlow()
    private val _typeValues: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val typeValues = _typeValues.asStateFlow()
    private val _genderValues: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val genderValues = _genderValues.asStateFlow()

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun refresh() {
        coroutineScope.launch(Dispatchers.IO) {
            _isRefreshing.update { true }
            appDb.clearAllTables()
            autoLoadEverything()
            getUniqueValuesFromDB()
            _isRefreshing.update { false }
        }
    }

    fun filterCharacters(
        name    : String = "",
        status  : String = "",
        species : String = "",
        type    : String = "",
        gender  : String = "",
    ) {
        val status = if (status != anyString) status else ""
        val species = if (species != anyString) species else ""
        val type = if (type != anyString) type else ""
        val gender = if (gender != anyString) gender else ""

        if (name.isBlank() && status.isBlank() && species.isBlank() && type.isBlank() && gender.isBlank()) {
            coroutineScope.launch {
                _filteredCharacters.update { _characters.value }
                if (_characters.value.isEmpty()) {
                    _filterMessage.update { application.getString(R.string.nothing_found) }
                } else {
                    _filterMessage.update { "" }
                }
                _filterMessage.update { "" }
            }
        }
        coroutineScope.launch {
            _isLoading.update { true }
            val filteredCharacters = dao.filterCharacters(
                name,
                status,
                species,
                type,
                gender
            )
            _filteredCharacters.update { filteredCharacters }
            if (filteredCharacters.isEmpty()) {
                _filterMessage.update { application.getString(R.string.nothing_found) }
            } else {

                _filterMessage.update { "" }
            }
            _isLoading.update { false }
        }
    }

    private suspend fun autoLoadEverything() {
        _isLoading.update { true }
        _errorMessage.update { "" }
        coroutineScope {
            async {
                try {
                    val characters = characterLoader.autoLoad()
                    _characters.update { characters }
                    if (10 <= characters.size) {
                        val firstTenCharacters = characters.subList(0, 10)
                        preloadImagesForCharacters(firstTenCharacters)
                    }
                    //_isLoading.update { false }
                    preloadImagesForCharacters(characters)
                } catch (e: Exception) {
                    _errorMessage.update { e.message ?: "" }
                    Log.e("tt4w", e.message, e)
                }
            }
            async {
                try {
                    _locations.update { locationLoader.autoLoad() }
                } catch (e: Exception) {
                    _errorMessage.update { e.message ?: "" }
                    Log.e("tt4w", e.message, e)
                }
            }
            async {
                try {
                    _episodes.update { episodeLoader.autoLoad() }
                } catch (e: Exception) {
                    _errorMessage.update { e.message ?: "" }
                    Log.e("tt4w", e.message, e)
                }
            }
        }.await()
        _isLoading.update { false }
    }

    private suspend fun getUniqueValuesFromDB() {
        val statusValues = (dao.getUniqueStatuses() + listOf(application.getString(R.string.any))).toMutableList()
        statusValues.removeAll { it == "" }
        _statusValues.update { statusValues }

        val typeValues = (dao.getUniqueTypes() + listOf(application.getString(R.string.any))).toMutableList()
        statusValues.removeAll { it == "" }
        _typeValues.update { typeValues }

        val speciesValues = (dao.getUniqueSpecies() + listOf(application.getString(R.string.any))).toMutableList()
        statusValues.removeAll { it == "" }
        _speciesValues.update { speciesValues }

        val genderValues = (dao.getUniqueGenders() + listOf(application.getString(R.string.any))).toMutableList()
        statusValues.removeAll { it == "" }
        _genderValues.update { genderValues }
    }

    private suspend fun preloadImagesForCharacters(imageLinks: List<CharacterDesc>) {
        coroutineScope {
            imageLinks.map { character ->
                async {
                    val request = ImageRequest.Builder(application)
                        .data(character.image)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build()
                    imageLoader.execute(request)
                }
            }.awaitAll()
        }
    }

    init {
        coroutineScope.launch(Dispatchers.IO) {
            autoLoadEverything()
            getUniqueValuesFromDB()
        }
    }
}