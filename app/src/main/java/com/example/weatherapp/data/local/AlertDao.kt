package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.pojo.WeatherAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(weatherAlert: WeatherAlert): Long

    @Query("Select * from Alert WHERE endDate + timeTo > :currentTime")
    fun getAllAerts(currentTime: Long): Flow<List<WeatherAlert>>

//    @Query("Delete FROM Alert WHERE endDate + timeTo < :currentTime")
//    suspend fun deleteAlerts(currentTime: Long):Int

    @Delete
    suspend fun deleteAlert(weatherAlert: WeatherAlert): Int
}