package com.mohit.weathersdk.network.db

import androidx.lifecycle.LiveData
import androidx.room.*

import com.mohit.weathersdk.network.db.TableProperties as tProp

@Dao
interface LocationDao {

    @Query("SELECT * FROM ${tProp.tableName}")
    fun getAllLocations(): LiveData<List<Location>>

    @Query("SELECT EXISTS(SELECT * FROM ${tProp.tableName} WHERE ${tProp.cityName} = :cityName)")
    fun isLocationAdded(cityName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(location: Location): Long

    @Query("UPDATE ${tProp.tableName} SET ${tProp.cityName} = :cityName, ${tProp.state} = :state, ${tProp.countryCode} = :countryCode, ${tProp.countryName} = :countryName, ${tProp.zipCode} = :zipCode WHERE ${tProp.id} = :id")
    fun update(id: Int, cityName: String,
               state: String, countryCode: String, countryName: String, zipCode: String)

    @Query("DELETE FROM ${tProp.tableName} WHERE ${tProp.id} = :id")
    fun delete(id: Int)

    @Query("DELETE FROM ${tProp.tableName}")
    fun deleteAll()
}