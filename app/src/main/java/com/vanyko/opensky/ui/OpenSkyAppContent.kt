package com.vanyko.opensky.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vanyko.opensky.ui.theme.OpenSkyTheme
import com.vanyko.opensky.R

@Composable
fun AppContent() {
    val navController = rememberNavController()
    OpenSkyTheme {
        Scaffold(
            topBar = {
                HomeTopAppBar()
            },
            bottomBar = {
                BottomNavigation(navController = navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                OpenSkyNavGraph(
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun HomeTopAppBar() {
    val title = stringResource(id = R.string.app_name)
    TopAppBar(
        title = {
            Text(text = title)
        },
        backgroundColor = MaterialTheme.colors.surface,
    )
}

sealed class Screen(val screen_route: String, val resourceId: Int, val imageVector: ImageVector) {
    object List : Screen(OpenSkyDestinations.LIST_ROUTE, R.string.list, Icons.Filled.List)
    object Map : Screen(OpenSkyDestinations.MAP_ROUTE, R.string.map, Icons.Filled.Map)
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        Screen.List,
        Screen.Map,
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.imageVector, contentDescription = stringResource(id = item.resourceId)) },
                label = { Text(text = stringResource(id = item.resourceId)) },
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OpenSkyTheme {
        // TODO: add app preview
    }
}