package io.github.starmage27.testtask4workmate

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.starmage27.testtask4workmate.ui.AppInfoScreen
import io.github.starmage27.testtask4workmate.ui.CharacterScreen
import io.github.starmage27.testtask4workmate.ui.EpisodeScreen
import io.github.starmage27.testtask4workmate.ui.HomeScreen
import io.github.starmage27.testtask4workmate.ui.LocationScreen
import io.github.starmage27.testtask4workmate.ui.theme.TestTask4WorkmateTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val owner = LocalViewModelStoreOwner.current
            owner?.let { owner ->
                viewModel = viewModel(
                    owner,
                    "MainViewModel",
                    MainViewModelFactory(
                        LocalContext.current.applicationContext as Application
                    )
                )
            }
            TestTask4WorkmateTheme {
                MainScreen()
            }
        }
    }

    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = HomeRoute,
        ) {
            composable<HomeRoute>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.KeepUntilTransitionsFinished },
            ) {
                HomeScreen(navController = navController, viewModel = viewModel)
            }
            composable<CharacterRoute>(
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { it / 4 })
                },
                exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { it / 4 })
                },
            ) { navigationStack ->
                val route = navigationStack.toRoute<CharacterRoute>()
                val id = route.id

                val characters = viewModel.characters.collectAsStateWithLifecycle()
                val character = characters.value.find{ it.id == id }
                
                character?.let { character ->
                    CharacterScreen(viewModel = viewModel, navController = navController, character = character)
                }
            }
            composable<LocationRoute>(
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { it / 4 })
                },
                exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { it / 4 })
                },
            ) { navigationStack ->
                val route = navigationStack.toRoute<LocationRoute>()
                val id = route.id

                val locations = viewModel.locations.collectAsStateWithLifecycle()
                val location = locations.value.find{ it.id == id }

                location?.let { location ->
                    LocationScreen(viewModel = viewModel, navController = navController, location = location)
                }
            }
            composable<EpisodeRoute>(
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { it / 4 })
                },
                exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { it / 4 })
                },
            ) { navigationStack ->
                val route = navigationStack.toRoute<EpisodeRoute>()
                val id = route.id

                val episodes = viewModel.episodes.collectAsStateWithLifecycle()
                val episode = episodes.value.find { it.id == id }

                episode?.let { episode ->
                    EpisodeScreen(viewModel = viewModel, navController = navController, episode = episode)
                }
            }
            composable<AppInfoRoute>(
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { it / 4 })
                },
                exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { it / 4 })
                },
            ) {
                AppInfoScreen(navController)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }
}