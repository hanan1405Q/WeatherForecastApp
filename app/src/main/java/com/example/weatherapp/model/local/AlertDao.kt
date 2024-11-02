package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.model.pojo.WeatherAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert
    suspend fun insertAlert(alert: WeatherAlert): Long

    @Delete
    suspend fun deleteAlert(alert: WeatherAlert)

    @Query("SELECT * FROM alerts ORDER BY startDate ASC")
    fun getAllAlerts(): Flow<MutableList<WeatherAlert>>

//    @Query("DELETE FROM alerts")
//    suspend fun deleteAllAlerts()

}
