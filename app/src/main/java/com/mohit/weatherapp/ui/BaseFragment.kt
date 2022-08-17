package com.mohit.weatherapp.ui

import com.mohit.weatherapp.di.ViewModelProviderFactory
import com.mohit.weatherapp.interfaces.IFragmentCallbacks
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    lateinit var iFragmentCallbacks: IFragmentCallbacks

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

}