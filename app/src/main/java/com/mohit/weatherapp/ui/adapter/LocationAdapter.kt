package com.mohit.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohit.weatherapp.databinding.ItemLocationBinding
import com.mohit.weathersdk.network.db.Location

class LocationAdapter : ListAdapter<Location, LocationAdapter.LocationViewHolder>(Companion) {

    var locationActionCallback: LocationActionCallback? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }

    interface LocationActionCallback {
        fun onDelete(location: Location)
        fun onLocation(location: Location)
    }

    class LocationViewHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)

    companion object: DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(layoutInflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)
        holder.binding.location = location
        holder.binding.callback = locationActionCallback
        holder.binding.executePendingBindings()
    }
}