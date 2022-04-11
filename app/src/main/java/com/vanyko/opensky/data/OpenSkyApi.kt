package com.vanyko.opensky.data

import com.vanyko.opensky.data.model.OpenSkyStateListApiModel
import retrofit2.Response

interface OpenSkyApi {
    suspend fun getAllStates(lamin: Float, lomin: Float, lamax: Float, lomax: Float): Response<OpenSkyStateListApiModel>
}