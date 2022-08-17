package com.mohit.weatherapp.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mohit.weatherapp.TestApplication
import com.mohit.weathersdk.network.api.WeatherApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class TestRetrofitModule {

    /**
     * The method returns the Context object
     */
    @Provides
    fun provideContext(app: TestApplication): Context {
        return app.applicationContext
    }

    /**
     * The method returns the Gson object
     */
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }


    /**
     * The method returns the OkHttpClient object
     */
    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {

        val requestInterceptor = Interceptor { chain ->
            val newUrl = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("appid", "fae7190d7e6433ec3a45285ffcf55c86")
                .build()
            val request = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
            return@Interceptor chain.proceed(request)
        }

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(requestInterceptor)
        okHttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        return okHttpClientBuilder.build()
    }


    /**
     * The method returns the MockWebServer object
     */
    @Provides
    @Singleton
    internal fun provideMockWebServer(): MockWebServer {
        return MockWebServer()
    }


    /**
     * The method returns the Retrofit object
     */
    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient, mockWebServer: MockWebServer): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(mockWebServer.url("/").toString())
            .client(okHttpClient)
            .build()
    }


    /**
     * We need the WeatherApi module.
     * For this, We need the Retrofit object, Gson and OkHttpClient .
     * So we will define the providers for these objects here in this module.
     */
    @Provides
    @Singleton
    internal fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}