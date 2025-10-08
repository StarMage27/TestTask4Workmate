package io.github.starmage27.testtask4workmate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.starmage27.testtask4workmate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(
    navController: NavController? = null,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { AppInfoTopBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            val textModifier = Modifier.padding(8.dp).fillMaxWidth()
            Text(
                modifier = textModifier,
                text = stringResource(R.string.this_app_is_made_as_a_test_task_for_workmate),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Text(
                modifier = textModifier,
                text = stringResource(R.string.author_dmitrii_kruglov),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoTopBar(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(R.string.about_app))
        },
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