package com.vanyko.opensky.ui.state_list

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanyko.opensky.R
import com.vanyko.opensky.data.model.OpenSkyState

@Composable
fun StateListScreen(
    stateListViewModel: StateListViewModel
) {
    val uiState by stateListViewModel.uiState.collectAsState()

    StateListScreen(
        uiState = uiState,
        onRefreshSkyStates = { stateListViewModel.refreshSkyStates() }
    )
}

@Composable
fun StateListScreen(
    uiState: ListUiState,
    onRefreshSkyStates: () -> Unit,
) {
    LoadingContent(
        empty = when (uiState) {
            is ListUiState.NoData -> uiState.isLoading
            else -> false
        },
        emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshSkyStates
    ) {
        when (uiState) {
            is ListUiState.HasData -> {
                SkyStatesList(
                    skyStates = uiState.skyStates
                )
            }
            is ListUiState.NoData -> {
                TextButton(
                    onClick = onRefreshSkyStates,
                    Modifier.fillMaxSize()
                ) {
                    Text(
                        stringResource(id = R.string.home_tap_to_load_content),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun SkyStatesList(
    skyStates: List<OpenSkyState>,
) {
    LazyColumn {
        items(items = skyStates) { skyState ->
            SkyStateCard(
                skyState = skyState
            )
        }
    }
}

@Composable
private fun SkyStateCard(
    skyState: OpenSkyState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        SkyStateCardContent(
            skyState = skyState
        )
    }
}

@Composable
private fun SkyStateCardContent(
    skyState: OpenSkyState
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(6.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(text = "${stringResource(id = R.string.callsign)}: ${skyState.callsign}")
            Text(text = "${stringResource(id = R.string.coordinates)}: [${skyState.longitude},${skyState.latitude}]")
            Text(text = "${stringResource(id = R.string.geo_altitude)}: ${skyState.geo_altitude} meters")

            if (expanded) {
                Text(text = "${stringResource(id = R.string.country)}: ${skyState.origin_country}")
                Text(text = "${stringResource(id = R.string.on_ground)}: ${skyState.on_ground}")
                Text(text = "${stringResource(id = R.string.baro_altitude)}: ${skyState.baro_altitude} meters")
                Text(text = "${stringResource(id = R.string.true_track)}: ${skyState.true_track} degrees")
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content,
        )
    }
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}