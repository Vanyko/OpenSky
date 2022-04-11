package com.vanyko.opensky.data.model

data class OpenSkyState(
    val icao24: String,
    val callsign: String?,
    val origin_country: String,
    val longitude: Float?,
    val latitude: Float?,
    val on_ground: Boolean,
    val baro_altitude: Float?,
    val geo_altitude: Float?,
    val true_track: Float?,
)
