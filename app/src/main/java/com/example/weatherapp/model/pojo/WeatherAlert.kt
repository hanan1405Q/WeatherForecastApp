package com.example.weatherapp.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class WeatherAlert (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var timeFrom: Long,
    var timeTo: Long,
    var startDate: Long,
    var endDate: Long,
    var type: Boolean
)

