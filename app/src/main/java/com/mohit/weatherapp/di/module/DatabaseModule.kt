package com.mohit.weatherapp.di.module

import android.content.Context
import com.mohit.weathersdk.network.db.AppDatabase
import com.mohit.weathersdk.network.db.LocationDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    /**
     * The method returns the AppDatabase object
     */
    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    /**
     * The method returns the LocationDao object
     */
    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }
}