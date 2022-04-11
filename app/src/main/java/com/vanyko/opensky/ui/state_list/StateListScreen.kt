package com.vanyko.opensky.ui.state_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(text = "${stringResource(id = R.string.callsign)}: ${skyState.callsign}")
            Text(text = "${stringResource(id = R.string.coordinates)}: [${skyState.longitude},${skyState.latitude}]")
            Text(text = "${stringResource(id = R.string.altitude)}: ${skyState.geo_altitude} meters")
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