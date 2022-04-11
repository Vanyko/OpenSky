package com.vanyko.opensky.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanyko.opensky.ui.state_list.StateListScreen
import com.vanyko.opensky.ui.state_list.StateListViewModel

object OpenSkyDestinations {
    const val LIST_ROUTE = "list"
    const val MAP_ROUTE = "map"
}

@Composable
fun OpenSkyNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = OpenSkyDestinations.LIST_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(OpenSkyDestinations.LIST_ROUTE) {
            val homeViewModel: StateListViewModel = hiltViewModel()
            StateListScreen(homeViewModel)
        }
        composable(OpenSkyDestinations.MAP_ROUTE) {
            Text(text = "Map screen")
        }
    }
}