package com.mohit.weathersdk.network.repo

import androidx.lifecycle.LiveData
import com.mohit.weathersdk.network.db.Location
import com.mohit.weathersdk.network.models.LocationForecastResponse
import com.mohit.weathersdk.network.models.LocationWeatherResponse
import com.mohit.weathersdk.util.Resource

/**
 * This provides the great flexibility for the concrete repos to have different implementation.
 */
interface WeatherRepository {

    fun getLocationWeatherByCityName(cityName: String): LiveData<Resource<LocationWeatherResponse>>

    fun getLocationForecast(lat: Double, lon: Double): LiveData<Resource<LocationForecastResponse>>

    fun getAllLocations(): LiveData<List<Location>>

    fun insertLocation(location: Location): LiveData<Resource<Long>>

    fun deleteLocation(location: Location): LiveData<Resource<Unit>>
}