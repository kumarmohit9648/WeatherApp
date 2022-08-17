package com.mohit.weathersdk.network.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

object TableProperties {
    const val tableName: String = "location"
    const val id: String = "id"
    const val cityName: String = "city_name"
    const val state: String = "administrative_area_level_1"
    const val countryCode: String = "country_code"
    const val countryName: String = "country_name"
    const val zipCode: String = "zip_code"
    const val lat: String = "lat"
    const val lon: String = "lon"
}

@Parcelize
@Entity(tableName = TableProperties.tableName)
data class Location (
    @ColumnInfo(name = TableProperties.cityName) var cityName: String? = null,
    @ColumnInfo(name = TableProperties.state) var administrativeAreaLevel1: String? = null,
    @ColumnInfo(name = TableProperties.countryCode) var countryCode: String? = null,
    @ColumnInfo(name = TableProperties.countryName) var countryName: String? = null,
    @ColumnInfo(name = TableProperties.zipCode) var zipCode: String? = null,
    @ColumnInfo(name = TableProperties.lat) var lat: Double? = null,
    @ColumnInfo(name = TableProperties.lon) var lon: Double? = null
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}