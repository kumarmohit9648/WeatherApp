package com.mohit.weatherapp.base

import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.mockwebserver.MockWebServer
import java.io.File

abstract class BaseTest {

    lateinit var testCoroutineDispatcher: TestCoroutineDispatcher

    lateinit var mockWebServer: MockWebServer

    lateinit var weatherRepository: com.mohit.weathersdk.network.repo.DefaultWeatherRepository

    protected fun getJson(path: String): String {
        this.javaClass.classLoader?.let {
            val uri = it.getResource(path)
            val file = File(uri.path)
            return String(file.readBytes())
        } ?: throw Exception("No class loader")
    }
}