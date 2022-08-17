package com.mohit.weatherapp.di.module

import com.mohit.weatherapp.ui.AppInfoFragment
import com.mohit.weatherapp.ui.LocationsFragment
import com.mohit.weatherapp.ui.WeatherInfoFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeLocationsFragment(): LocationsFragment

    @ContributesAndroidInjector
    abstract fun contributeWeatherInfoFragment(): WeatherInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeAppInfoFragment(): AppInfoFragment
}