package com.mohit.weatherapp.di.module

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mohit.weatherapp.MyApp
import com.mohit.weathersdk.network.api.WeatherApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    /**
     * The method returns the Context object
     */
    @Provides
    fun provideContext(app: MyApp): Context {
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
     * The method returns the HttpLoggingInterceptor object
     */
    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("Network", message)
                }
            }
        )
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        return httpLoggingInterceptor
    }


    /**
     * The method returns the OkHttpClient object
     */
    @Provides
    @Singleton
    internal fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val requestInterceptor = Interceptor { chain ->
            val newUrl = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("appid", "ae1c4977a943a50eaa7da25e6258d8b2")
                .build()
            val request = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
            return@Interceptor chain.proceed(request)
        }

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(httpLoggingInterceptor)
        httpClient.addInterceptor(requestInterceptor)
        httpClient.writeTimeout(60, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        return httpClient.build()
    }


    /**
     * The method returns the Retrofit object
     */
    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://api.openweathermap.org/data/2.5/")
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