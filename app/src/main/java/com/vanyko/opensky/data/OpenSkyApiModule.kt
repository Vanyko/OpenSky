package com.vanyko.opensky.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class OpenSkyApiModule {
    private val _baseUrl = "https://opensky-network.org/api/"

    @Provides
    fun provideAnalyticsService(): OpenSkyApi {
        return RetrofitClient.getClient(_baseUrl).create(OpenSkyApiService::class.java)
    }
}