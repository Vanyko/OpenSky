package com.vanyko.opensky.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.vanyko.opensky.data.model.OpenSkyState
import com.vanyko.opensky.ui.state_list.ListUiState
import com.vanyko.opensky.ui.state_list.StateListViewModel
import com.vanyko.opensky.R

@Composable
fun MapScreen(
    stateListViewModel: StateListViewModel
) {
    val uiState by stateListViewModel.uiState.collectAsState()

    MapScreen(uiState = uiState)
}

@Composable
fun MapScreen(
    uiState: ListUiState,
) {
    val czechiaBounds = LatLngBounds(
        LatLng((48.55), 12.9),  // SW bounds
        LatLng((51.06), 18.87)  // NE bounds
    )

    GoogleMapScreen(
        skyStates =
            if (uiState is ListUiState.HasData) {
                uiState.skyStates
            }
            else {
                emptyList()
            },
        bounds = czechiaBounds
    )
}

@Composable
fun GoogleMapScreen(
    skyStates: List<OpenSkyState>,
    bounds: LatLngBounds
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(bounds.center, 6f)
        }

        val maxAltitude = skyStates.maxOfOrNull { it.geo_altitude?: 1f } ?: 1f

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            skyStates
                .filter { it.latitude != null && it.longitude != null }
                .forEach { skyState ->
                    val planeLocation = LatLng(skyState.latitude!!.toDouble(), skyState.longitude!!.toDouble())
                    val icon = getBitmapDescriptorFromVector(
                        LocalContext.current,
                        R.drawable.airplane,
                        0.15f + (0.15f / maxAltitude) * (skyState.geo_altitude ?: 0f)
                    )
                    Marker(
                        position = planeLocation,
                        title = skyState.callsign,
                        icon = icon,
                        rotation = skyState.true_track ?: 0f
                    )
                }
        }
    }
}

fun getBitmapDescriptorFromVector(
    context: Context,
    @DrawableRes vectorDrawableResourceId: Int,
    sizeModifier: Float = 1f
): BitmapDescriptor {

    val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
    var bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    bitmap = Bitmap.createScaledBitmap(
        bitmap,
        (vectorDrawable.intrinsicWidth * sizeModifier).toInt(),
        (vectorDrawable.intrinsicHeight * sizeModifier).toInt(),
        false
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(
        0,
        0,
        canvas.width,
        canvas.height
    )
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}