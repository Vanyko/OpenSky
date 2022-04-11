package com.vanyko.opensky.data

import com.vanyko.opensky.common.IoDispatcher
import com.vanyko.opensky.data.model.OpenSkyState
import com.vanyko.opensky.data.model.OpenSkyStateListApiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenSkyRepository @Inject constructor (
    private val openSkyApi: OpenSkyApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchSkyStates(
        lamin: Float = 48.55F,
        lomin: Float = 12.9F,
        lamax: Float = 51.06F,
        lomax: Float = 18.87F
    ): Result<List<OpenSkyState>> =
        withContext(ioDispatcher) {
            val response = openSkyApi.getAllStates(lamin, lomin, lamax, lomax)
            if (response.isSuccessful) {
                Result.Success(response.body()?.toOpenSkyStateList() ?: emptyList())
            } else {
                Result.Error("Failed to get ske state list")
            }
        }
}

private fun OpenSkyStateListApiModel.toOpenSkyStateList(): List<OpenSkyState> {
    val stateList = mutableListOf<OpenSkyState>()

    states.map { array ->
        stateList.add(
            OpenSkyState(
                icao24 = array[0].toString(),
                callsign = array[1],
                origin_country = array[2].toString(),
                longitude = array[5]?.toFloatOrNull(),
                latitude = array[6]?.toFloatOrNull(),
                baro_altitude = array[7]?.toFloatOrNull(),
                on_ground = array[8].toBoolean(),
                true_track = array[10]?.toFloatOrNull(),
                geo_altitude = array[13]?.toFloatOrNull(),
            )
        )
    }

    return stateList
}