package com.mohit.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mohit.weathersdk.network.models.LocationWeatherResponse
import com.mohit.weathersdk.network.db.Location
import com.mohit.weathersdk.network.models.LocationForecastResponse
import com.mohit.weathersdk.network.repo.WeatherRepository
import com.mohit.weathersdk.util.Resource

import javax.inject.Inject

/**
 * Common ViewModel class for Weather Module.
 * As we are using coroutines with livedata which is introduced as a part of lifecycle-runtime-ktx artifact in 2.2.0,
 * Our ViewModel is very simple due to livedata lifecycle awareness.
 * It has only WeatherRepository as a dependency in order to provide data.
 */
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    fun getLocationWeatherByCityName(cityName: String): LiveData<Resource<LocationWeatherResponse>> {
        return weatherRepository.getLocationWeatherByCityName(cityName)
    }

    fun getLocationForecast(lat: Double, lon: Double): LiveData<Resource<LocationForecastResponse>> {
        return weatherRepository.getLocationForecast(lat, lon)
    }

    fun getAllLocations(): LiveData<List<Location>> {
        return weatherRepository.getAllLocations()
    }

    fun insertLocation(location: Location): LiveData<Resource<Long>> {
        return weatherRepository.insertLocation(location)
    }

    fun deleteLocation(location: Location): LiveData<Resource<Unit>> {
        return weatherRepository.deleteLocation(location)
    }

}