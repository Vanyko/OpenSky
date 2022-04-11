package com.vanyko.opensky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanyko.opensky.ui.state_list.StateListScreen
import com.vanyko.opensky.ui.state_list.StateListViewModel
import com.vanyko.opensky.ui.theme.OpenSkyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenSkyTheme {
                Scaffold(
                    topBar = {
                        HomeTopAppBar()
                    }
                ) {
                    val homeViewModel: StateListViewModel = hiltViewModel()
                    StateListScreen(homeViewModel)
                }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OpenSkyTheme {
        // TODO: add app preview
    }
}