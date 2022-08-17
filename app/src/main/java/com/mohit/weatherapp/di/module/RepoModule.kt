package com.mohit.weatherapp.di.module

import android.content.Context
import com.mohit.weathersdk.network.api.WeatherApi
import com.mohit.weathersdk.network.db.LocationDao
import com.mohit.weathersdk.network.repo.DefaultWeatherRepository
import com.mohit.weathersdk.network.repo.WeatherRepository
import com.mohit.weathersdk.util.DefaultDispatcherProvider
import com.mohit.weathersdk.util.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module
class RepoModule {



    /**
     * The method returns the DispatcherProvider object
     */
    @Provides
    fun provideDefaultDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    /**
     * The method returns the DefaultWeatherRepository object
     */
    @Provides
    fun provideDefaultWeatherRepository(
        context: Context,
        dispatcherProvider: DefaultDispatcherProvider,
        locationDao: LocationDao,
        weatherApi: WeatherApi
    ): WeatherRepository {
        return DefaultWeatherRepository(
            context,
            dispatcherProvider,
            locationDao,
            weatherApi
        )
    }
}