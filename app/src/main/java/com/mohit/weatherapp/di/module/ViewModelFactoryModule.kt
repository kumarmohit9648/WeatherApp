package com.mohit.weatherapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.mohit.weatherapp.di.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProvideFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}