package io.github.starmage27.testtask4workmate.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import io.github.starmage27.testtask4workmate.EpisodeRoute
import io.github.starmage27.testtask4workmate.HomeViewModel
import io.github.starmage27.testtask4workmate.LocationRoute
import io.github.starmage27.testtask4workmate.R
import io.github.starmage27.testtask4workmate.data.character.CharacterDesc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    viewModel: HomeViewModel? = null,
    navController: NavController? = null,
    character: CharacterDesc,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val episodes = viewModel?.episodes?.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PageTopBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
                name = character.name
            )
        }

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            val textModifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    //color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp,
                    tonalElevation = 2.dp
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        model = character.image,
                        contentDescription = stringResource(R.string.picture_of, character.name),
                    )
                }
            }

            item {
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.status)}: ${character.status}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.species)}: ${character.species}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
                if (character.type.isNotBlank()) {
                    Text(
                        modifier = textModifier,
                        text = "${stringResource(R.string.type)}: ${character.type}",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp
                    )
                }
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.gender)}: ${character.gender}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
            }
            item { HorizontalDivider() }
            item {
                val locationId = try {
                    character.location.url.filter{ it.isDigit() }.toInt()
                } catch (_: Exception) {
                    0
                }

                val originId = try {
                    character.origin.url.filter{ it.isDigit() }.toInt()
                } catch (_: Exception) {
                    0
                }

                Surface(
                    onClick = {
                        navController?.navigate(LocationRoute(locationId))
                    },
                    modifier = Modifier.padding(4.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Text(
                        modifier = textModifier,
                        text = "${stringResource(R.string.location)}: ${character.location.name}",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp
                    )
                }

                Surface(
                    onClick = {
                        navController?.navigate(LocationRoute(originId))
                    },
                    modifier = Modifier.padding(4.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Text(
                        modifier = textModifier,
                        text = "${stringResource(R.string.origin)}: ${character.origin.name}",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp
                    )
                }
            }
            item { HorizontalDivider() }
            item {
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.appearances)}:",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
            }
            itemsIndexed(character.episode) { index, episode ->
                val episodeId = try {
                    episode.filter{ it.isDigit() }.toInt()
                } catch (_: Exception) {
                    0
                }
                val episode = episodes?.value?.filter { it.id == episodeId }?.getOrNull(0)
                val episodeNum = episode?.episode?:""
                val episodeName = episode?.name?:""

                Surface(
                    onClick = {
                        navController?.navigate(EpisodeRoute(episodeId))
                    },
                    modifier = Modifier.padding(4.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Text(
                        text = "[$episodeNum] $episodeName",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            item { HorizontalDivider() }
            item {
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.created)}: ${character.created}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
                Text(
                    modifier = textModifier,
                    text = "ID: ${character.id}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTopBar(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    name: String
) {
    LargeTopAppBar(
        modifier = modifier,
        title = { Text(text = name) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = {
                    navController?.navigateUp()
                }
            ) {
                Icon(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.back),
                )
            }
        }
    )
}