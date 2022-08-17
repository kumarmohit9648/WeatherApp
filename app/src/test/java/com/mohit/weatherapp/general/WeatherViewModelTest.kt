package com.mohit.weatherapp.general

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.mohit.weatherapp.TestApplication
import com.mohit.weathersdk.util.getOrAwaitValues
import com.mohit.weatherapp.base.BaseTest
import com.mohit.weathersdk.util.Status
import com.mohit.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.HttpURLConnection

/**
 * Unit test for [WeatherViewModel]
 */
@Config(application = TestApplication::class)
@RunWith(RobolectricTestRunner::class)
class WeatherViewModelTest : BaseTest() {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check and verify that the 'getLocationWeatherByCityName' succeeds with 200`() = runBlockingTest {

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("getLocationWeather.json")))

        val weatherViewModel = WeatherViewModel(weatherRepository)

        val results = weatherViewModel.getLocationWeatherByCityName("London").getOrAwaitValues()
        val initialResult = results[0]
        val result = results[1]

        Truth.assertThat(initialResult.status).isEqualTo(Status.LOADING)
        Truth.assertThat(result.status).isEqualTo(Status.SUCCESS)
        val weatherResponse = result.data
        Truth.assertThat(weatherResponse).isNotNull()
        Truth.assertThat(weatherResponse?.coord).isNotNull()
        Truth.assertThat(weatherResponse?.weather).isNotNull()
        Truth.assertThat(weatherResponse?.weather?.get(0)?.id).isEqualTo(300)
        Truth.assertThat(weatherResponse?.weather?.get(0)?.main).isEqualTo("Drizzle")
        Truth.assertThat(weatherResponse?.weather?.get(0)?.description).isEqualTo("light intensity drizzle")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check and verify that the 'getLocationWeatherByCityName' fails with 502 bad gateway`() = runBlockingTest {

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY)
            .setBody(""))

        val weatherViewModel = WeatherViewModel(weatherRepository)

        val results = weatherViewModel.getLocationWeatherByCityName("London").getOrAwaitValues()
        val initialResult = results[0]
        val result = results[1]

        Truth.assertThat(initialResult.status).isEqualTo(Status.LOADING)
        Truth.assertThat(result.status).isEqualTo(Status.ERROR)
        Truth.assertThat(result.data).isNull()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check and verify that the 'getLocationWeatherByCityName' fails with 503`() = runBlockingTest {

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAVAILABLE)
            .setBody(getJson("internalServerFailureWith503.json")))

        val weatherViewModel = WeatherViewModel(weatherRepository)

        val results = weatherViewModel.getLocationWeatherByCityName("London").getOrAwaitValues()
        val initialResult = results[0]
        val result = results[1]

        Truth.assertThat(initialResult.status).isEqualTo(Status.LOADING)
        Truth.assertThat(result.status).isEqualTo(Status.ERROR)
        Truth.assertThat(result.data).isNull()
        Truth.assertThat(result.msg).isEqualTo("Unexpected Error.")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check and verify that the 'getLocationWeatherByCityName' fails with 401`() = runBlockingTest {

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
            .setBody(getJson("invalidAppId.json")))

        val weatherViewModel = WeatherViewModel(weatherRepository)

        val results = weatherViewModel.getLocationWeatherByCityName("London").getOrAwaitValues()
        val initialResult = results[0]
        val result = results[1]

        Truth.assertThat(initialResult.status).isEqualTo(Status.LOADING)
        Truth.assertThat(result.status).isEqualTo(Status.ERROR)
        Truth.assertThat(result.data).isNull()
        Truth.assertThat(result.msg).isEqualTo("Invalid App Id")
    }
}