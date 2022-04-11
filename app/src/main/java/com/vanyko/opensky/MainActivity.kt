package com.vanyko.opensky

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vanyko.opensky.data.OpenSkyApi
import com.vanyko.opensky.ui.theme.OpenSkyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var openSkyApi: OpenSkyApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenSkyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }

        runBlocking {
            val response = openSkyApi.getAllStates(
                lamin = 48.55F,
                lomin = 12.9F,
                lamax = 51.06F,
                lomax = 18.87F
            )

            if (response.isSuccessful) {
                Log.i("MainActivity", "Success: " + response.body().toString())
            } else {
                Log.i("MainActivity", "Failure: " + response.message())
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OpenSkyTheme {
        Greeting("Android")
    }
}