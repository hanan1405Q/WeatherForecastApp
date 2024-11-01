package com.example.weatherapp.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "favorite_locations")
data class UserLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lat: Double,
    val lon: Double
) {
    // Secondary constructor without id
    constructor(lat: Double, lon: Double) : this(0, lat, lon)
}