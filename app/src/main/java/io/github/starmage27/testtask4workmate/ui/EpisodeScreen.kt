package io.github.starmage27.testtask4workmate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.github.starmage27.testtask4workmate.CharacterRoute
import io.github.starmage27.testtask4workmate.HomeViewModel
import io.github.starmage27.testtask4workmate.R
import io.github.starmage27.testtask4workmate.data.episode.EpisodeDesc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeScreen(
    viewModel: HomeViewModel? = null,
    navController: NavController? = null,
    episode: EpisodeDesc,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val characters = viewModel?.characters?.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PageTopBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
                name = episode.name
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
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.episode)}: ${episode.episode}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.air_date)}: ${episode.air_date}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
            }
            item { HorizontalDivider() }
            item {
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.characters)}:",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
            }
            itemsIndexed(episode.characters) { index, resident ->
                val characterId = try {
                    (resident.filter { it.isDigit() }).toInt()
                } catch (_: Exception) {
                    0
                }
                val characterName = characters?.let { characters ->
                    (characters.value.find { it.id == characterId })?.name
                }?:""

                Surface(
                    onClick = {
                        navController?.navigate(CharacterRoute(characterId))
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Text(
                        text = characterName,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            item { HorizontalDivider() }
            item {
                Text(
                    modifier = textModifier,
                    text = "${stringResource(R.string.created)}: ${episode.created}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
                Text(
                    modifier = textModifier,
                    text = "ID: ${episode.id}",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp
                )
            }
        }
    }
}