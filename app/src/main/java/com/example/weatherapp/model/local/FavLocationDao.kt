package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp.model.pojo.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FavLocationDao {
    @Insert
    suspend fun insertLocation(location: UserLocation): Long

    @Update
    suspend fun updateLocation(location: UserLocation)

    @Delete
    suspend fun deleteLocation(location: UserLocation)

    @Query("SELECT * FROM favorite_locations ORDER BY id ASC")
    fun getAllLocations(): Flow<MutableList<UserLocation>>

    @Query("DELETE FROM favorite_locations")
    suspend fun deleteAllLocations()


}