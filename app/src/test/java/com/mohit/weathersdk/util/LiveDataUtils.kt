package com.mohit.weathersdk.util

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValues(
    time: Long = 10,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): List<T> {
    val values = mutableListOf<T>()
    val latch = CountDownLatch(2)
    val observer = object : Observer<T> {
        override fun onChanged(o: T) {
            values.add(o)
            latch.countDown()
            if (latch.count.toInt() <= 0) {
                this@getOrAwaitValues.removeObserver(this)
            }
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    return values
}