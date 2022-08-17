package com.mohit.weathersdk.network.api

import com.mohit.weathersdk.network.models.LocationForecastResponse
import com.mohit.weathersdk.network.models.LocationWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(value = "weather")
    suspend fun getLocationWeatherByCityName(
        @Query(value = "q") cityName: String,
        @Query(value = "units") units: String = "imperial"
    ): Response<LocationWeatherResponse>

    @GET(value = "forecast")
    suspend fun getLocationForecast(
        @Query(value = "lat") lat: Double,
        @Query(value = "lon") lon: Double
    ): Response<LocationForecastResponse>

}