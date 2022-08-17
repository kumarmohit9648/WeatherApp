package com.mohit.weathersdk.network.models

data class LocationForecastResponse (
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListElement>,
    val city: City
)
