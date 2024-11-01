//package com.example.weatherapp.data.local
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//import com.example.weatherapp.data.pojo.UserLocation
//import com.example.weatherapp.data.pojo.WeatherAlert
//
//@Database(entities = [WeatherAlert::class, UserLocation::class], version = 1)
//
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun alertDao(): AlertDao
//    abstract fun userFavLocationDao(): UserFavLocationDao
//}
//
//object DatabaseInstance {
//    @Volatile
//    private var INSTANCE: AppDatabase? = null
//
//    fun getDatabase(context: Context): AppDatabase {
//        return INSTANCE ?: synchronized(this) {
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "weather_app_database"
//            ).build()
//            INSTANCE = instance
//            instance
//        }
//    }
//}