package com.mohit.weathersdk.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import javax.inject.Inject

/**
 * This is used by tests to provide the dispatcher.
 */
@ExperimentalCoroutinesApi
class TestDispatcherProvider @Inject constructor(private val testCoroutineDispatcher: TestCoroutineDispatcher) :
    DispatcherProvider {
    override fun main(): CoroutineDispatcher = testCoroutineDispatcher
    override fun default(): CoroutineDispatcher = testCoroutineDispatcher
    override fun io(): CoroutineDispatcher = testCoroutineDispatcher
    override fun unconfined(): CoroutineDispatcher = testCoroutineDispatcher
}