package com.example.weatherapp.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "favorite_locations")
data class UserLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lat: Double,
    val lon: Double,
)