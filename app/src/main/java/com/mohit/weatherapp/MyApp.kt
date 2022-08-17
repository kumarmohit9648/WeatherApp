package com.mohit.weatherapp

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.google.android.libraries.places.api.Places
import com.mohit.weatherapp.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class MyApp : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        Places.initialize(this, BuildConfig.GOOGLE_API_KEY)
        // Init Dagger2
        this.initDagger()
    }

    protected open fun initDagger() = DaggerAppComponent.builder()
        .create(this)
        .inject(this)

}