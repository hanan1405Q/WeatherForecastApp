package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.local.AlertDao
import com.example.weatherapp.model.local.FavLocationDao
import com.example.weatherapp.model.pojo.UserLocation
import com.example.weatherapp.model.pojo.WeatherAlert

@Database(entities = [WeatherAlert::class, UserLocation::class], version = 1)

abstract class WeatherDatabase : RoomDatabase() {
    abstract fun alertDao(): AlertDao
    abstract fun FavLocationDao(): FavLocationDao
   companion object{
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

