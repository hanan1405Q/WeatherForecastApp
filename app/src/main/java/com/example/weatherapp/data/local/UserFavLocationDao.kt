package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.pojo.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavLocationDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertLocation(location: UserLocation)

        @Delete
        suspend fun deleteLocation(location: UserLocation)

        @Query("SELECT * FROM favorite_locations")
        fun getAllLocations(): Flow<List<UserLocation>>

}