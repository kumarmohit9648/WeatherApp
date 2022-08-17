package com.mohit.weathersdk.network.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mohit.weathersdk.network.db.Location
import com.mohit.weathersdk.network.models.LocationWeatherResponse
import com.mohit.weathersdk.network.api.WeatherApi
import com.mohit.weathersdk.network.db.LocationDao
import com.mohit.weathersdk.network.models.LocationForecastResponse
import com.mohit.weathersdk.util.DispatcherProvider
import com.mohit.weathersdk.util.Resource
import javax.inject.Inject

/**
 * This is a Main repo provided for WeatherModule.
 * The coroutine context used here is dispatcherProvider.io()
 */
class DefaultWeatherRepository @Inject constructor(
    override val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val locationDao: LocationDao,
    private val weatherApi: WeatherApi
) : BaseRepository(context), WeatherRepository {

    /**
     * API to get Location Weather By CityName.
     */
    override fun getLocationWeatherByCityName(
        cityName: String
    ): LiveData<Resource<LocationWeatherResponse>> =
        liveData(dispatcherProvider.io()) {
            emit(Resource.loading())
            val result = safeApiCall { weatherApi.getLocationWeatherByCityName(cityName) }
            emit(result)
        }

    override fun getLocationForecast(
        lat: Double,
        lon: Double
    ): LiveData<Resource<LocationForecastResponse>> =
        liveData(dispatcherProvider.io()) {
            emit(Resource.loading())
            val result = safeApiCall { weatherApi.getLocationForecast(lat, lon) }
            emit(result)
        }

    override fun getAllLocations(): LiveData<List<Location>> = locationDao.getAllLocations()

    override fun insertLocation(location: Location): LiveData<Resource<Long>> =
        liveData(dispatcherProvider.io()) {
            emit(Resource.loading())
            val result = locationDao.insert(location)
            emit(Resource.success(result))
        }

    override fun deleteLocation(location: Location): LiveData<Resource<Unit>> =
        liveData(dispatcherProvider.io()) {
            emit(Resource.loading())
            val result = locationDao.delete(location.id)
            emit(Resource.success(result))
        }

}