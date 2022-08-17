package com.mohit.weathersdk.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.BindingAdapter


@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["setVisible"])
fun View.bindVisibility(makeVisible: Boolean) {
    this.run {
        visibility = if (makeVisible) View.VISIBLE else View.INVISIBLE
    }
}