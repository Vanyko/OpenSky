package com.vanyko.opensky.data

import com.vanyko.opensky.data.model.OpenSkyStateListApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenSkyApiService: OpenSkyApi {

    @GET("states/all")
    override suspend fun getAllStates(
        @Query("lamin") lamin: Float,
        @Query("lomin") lomin: Float,
        @Query("lamax") lamax: Float,
        @Query("lomax") lomax: Float,
    ): Response<OpenSkyStateListApiModel>
}