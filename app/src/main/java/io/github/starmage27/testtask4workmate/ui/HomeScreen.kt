package io.github.starmage27.testtask4workmate.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import io.github.starmage27.testtask4workmate.AppInfoRoute
import io.github.starmage27.testtask4workmate.CharacterRoute
import io.github.starmage27.testtask4workmate.HomeViewModel
import io.github.starmage27.testtask4workmate.R
import io.github.starmage27.testtask4workmate.data.character.CharacterDesc
import io.github.starmage27.testtask4workmate.ui.theme.TestTask4WorkmateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel? = null,
    navController: NavController? = null,
) {
    val imageLoader = viewModel?.getImageLoader()
    val characters = viewModel?.characters?.collectAsStateWithLifecycle()
    val isLoading = viewModel?.isLoading?.collectAsStateWithLifecycle()
    val errorMessage = viewModel?.errorMessage?.collectAsStateWithLifecycle()
    val isRefreshing = viewModel?.isRefreshing?.collectAsStateWithLifecycle()

    val loadingDpAnimated = animateFloatAsState(
        targetValue = if (isLoading?.value == true) 1f else 0f,
        label = "Loading bar animation",
    )

    var topOffset by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    Scaffold { paddingValues ->
        CharacterSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    with(density) {
                        topOffset = size.height.toDp().value
                    }
                }
                .padding(4.dp)
            ,
            navController = navController,
            viewModel = viewModel,
            imageLoader = imageLoader,
            loadingDpAnimated = loadingDpAnimated.value
        )
        Column(
            modifier = Modifier
                //.padding(paddingValues = paddingValues)
                //.padding(top = topOffset.dp)
            ,
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp * loadingDpAnimated.value)
                    .alpha(loadingDpAnimated.value)
                    .offset(y = topOffset.dp)
                ,
                color = MaterialTheme.colorScheme.primary
            )


            PullToRefreshBox(
                isRefreshing = isRefreshing?.value ?: false,
                onRefresh = { viewModel?.refresh() },
                modifier = Modifier
                    .fillMaxSize()
                ,
            ) {
                CharacterGrid(
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                    characters = characters?.value ?: emptyList(),
                    navController = navController,
                    imageLoader = imageLoader,
                    topOffset = topOffset.dp + paddingValues.calculateTopPadding()
                )

                val isError = errorMessage?.value != ""
                if (isError) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                        ,
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.onError,
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = errorMessage?.value ?: "",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterGrid(
    modifier: Modifier = Modifier,
    characters: List<CharacterDesc>,
    navController: NavController? = null,
    imageLoader: ImageLoader? = null,
    topOffset: Dp = 0.dp
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        if (topOffset > 0.dp) {
            items(2) {
                Box(modifier = Modifier.size(topOffset))
            }
        }
        itemsIndexed(characters) { index, character ->
            CharacterIcon(
                modifier = Modifier,
                character = character,
                navController = navController,
                imageLoader = imageLoader
            )
        }
        if (characters.isEmpty()) {
            items(20) {
                CharacterIcon(
                    modifier = Modifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterSearchBar(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: HomeViewModel? = null,
    imageLoader: ImageLoader? = null,
    loadingDpAnimated: Float = 0f
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val any = stringResource(R.string.any)

    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(any) }
    var species by remember { mutableStateOf(any) }
    var type by remember { mutableStateOf(any) }
    var gender by remember { mutableStateOf(any) }

    val statusValues = viewModel?.statusValues?.collectAsStateWithLifecycle()
    val speciesValues = viewModel?.speciesValues?.collectAsStateWithLifecycle()
    val typeValues = viewModel?.typeValues?.collectAsStateWithLifecycle()
    val genderValues = viewModel?.genderValues?.collectAsStateWithLifecycle()

    val searchResults = viewModel?.filteredCharacters?.collectAsStateWithLifecycle()
    val filterMessage = viewModel?.filterMessage?.collectAsStateWithLifecycle()

    fun applyFilters() {
        viewModel?.filterCharacters(name, status, species, type, gender)
    }
    fun resetFilters() {
        name = ""
        status = any
        species = any
        type = any
        gender = any
        applyFilters()
    }

    SearchBar(
        modifier = modifier
            .semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = name,
                onQueryChange = { name = it },
                onSearch = {
                    Log.i("tt4w", "onSearch")
                    applyFilters()
                },
                expanded = expanded,
                onExpandedChange = { isExpanded ->
                    expanded = isExpanded
                },
                placeholder = { Text(stringResource(R.string.search)) },
                leadingIcon = {
                    val iconResource = if (expanded)
                        R.drawable.baseline_arrow_back_24
                    else
                        R.drawable.baseline_search_24

                    IconButton(
                        onClick = {
                            expanded = !expanded
                            if (!expanded) {
                                resetFilters()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(iconResource),
                            contentDescription = stringResource(R.string.search),
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            navController?.navigate(AppInfoRoute)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_info_24),
                            contentDescription = stringResource(R.string.search),
                            modifier = Modifier,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { isExpanded ->
            expanded = isExpanded
            resetFilters()
        },
    ) {
        Column {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp * loadingDpAnimated)
                    .alpha(loadingDpAnimated)
                ,
                color = MaterialTheme.colorScheme.primary
            )
            LazyRow {
                val dropdownModifier = Modifier
                    .size(width = 256.dp, height = 64.dp)
                    .padding(2.dp)
                item {
                    DropdownMenu(
                        modifier = dropdownModifier,
                        label = stringResource(R.string.status),
                        onClick = { selectedElement ->
                            status = selectedElement
                            applyFilters()
                        },
                        elements = statusValues?.value?: emptyList()
                    )
                }
                item {
                    DropdownMenu(
                        modifier = dropdownModifier,
                        label = stringResource(R.string.species),
                        onClick = { selectedElement ->
                            species = selectedElement
                            applyFilters()
                        },
                        elements = speciesValues?.value?: emptyList()
                    )
                }
                item {
                    DropdownMenu(
                        modifier = dropdownModifier,
                        label = stringResource(R.string.type),
                        onClick = { selectedElement ->
                            type = selectedElement
                            applyFilters()
                        },
                        elements = typeValues?.value?: emptyList()
                    )
                }
                item {
                    DropdownMenu(
                        modifier = dropdownModifier,
                        label = stringResource(R.string.gender),
                        onClick = { selectedElement ->
                            gender = selectedElement
                            applyFilters()
                        },
                        elements = genderValues?.value?: emptyList()
                    )
                }
            }

            if (searchResults?.value?.isNotEmpty() ?: false) {
                CharacterGrid(
                    modifier = Modifier,
                    characters = searchResults.value,
                    navController = navController,
                    imageLoader = imageLoader
                )
            } else {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = filterMessage?.value ?: stringResource(R.string.enter_your_filters_and_press_search),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}

@Composable
fun CharacterIcon(
    modifier: Modifier,
    character: CharacterDesc? = null,
    navController: NavController? = null,
    imageLoader: ImageLoader? = null,
) {
    val color = if (character == null) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
    val name = character?.name ?: stringResource(R.string.loading)
    val species = character?.species ?: stringResource(R.string.loading)
    val gender = character?.gender ?: stringResource(R.string.loading)
    val status = character?.status ?: stringResource(R.string.loading)
    val statusColor = when (status) {
        "Alive" -> Color.Green
        "Dead" -> Color.Red
        else -> Color.Yellow
    }

    Surface(
        onClick = {
            character?.let { characterDescription ->
                navController?.navigate(CharacterRoute(characterDescription.id))
            }
        },
        modifier.padding(4.dp),
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            val imageModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
            Surface(
                modifier = imageModifier,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = imageModifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp)
                    )
                    if (character != null && imageLoader != null) {
                        AsyncImage(
                            modifier = imageModifier,
                            model = character.image,
                            contentDescription = stringResource(R.string.picture_of, character.name),
                            imageLoader = imageLoader
                        )
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Surface(
                                modifier = Modifier.padding(4.dp),
                                shape = RoundedCornerShape(100)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = status,
                                    color = statusColor,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = name,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = color
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = species,
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = color
                )
                Text(
                    text = gender,
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = color
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    TestTask4WorkmateTheme {
        HomeScreen()
    }
}