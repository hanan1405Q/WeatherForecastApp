package com.example.weatherapp.model.repository
import com.example.weatherapp.model.local.LocalDataSource
import com.example.weatherapp.model.pojo.CurrentWeatherResponse
import com.example.weatherapp.model.pojo.ForecastResponse
import com.example.weatherapp.model.pojo.UserLocation
import com.example.weatherapp.model.pojo.WeatherAlert
import com.example.weatherapp.model.remot.RetrofitHelper
import com.example.weatherapp.util.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRepo (val remote: RetrofitHelper, val local:LocalDataSource){

    companion object{
        @Volatile
        private var repository:WeatherRepo? = null
        fun getInstance(retrofitHelper:RetrofitHelper,local: LocalDataSource):WeatherRepo{
            return repository ?: synchronized(this){
                val temp =  WeatherRepo(retrofitHelper,local)
                repository = temp
                temp
            }
        }
    }
    //Remote function that returns a Flow<CurrentWeatherResponse>
    suspend fun getCurrentWeather(
        lat: Double, long: Double, unit: String, lang: String
    ): Flow<Response<CurrentWeatherResponse>> = flow {
        try {
            val result = remote.apiServices.getCurrentWeather(lat, long, unit, lang, Utils.API_KEY)
            emit(result)  // Emit the API result
        } catch (e: Exception) {
            throw e  // Propagate the exception so it can be handled downstream
        }
    }

    // Fetch full forecast data and emit it
    suspend fun getForecastWeather(
        lat: Double, long: Double, unit: String, lang: String
    ): Flow<ForecastResponse> = flow {
        try {
            val result = remote.apiServices.getForecastWeather(lat, long, unit, lang, Utils.API_KEY)
            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }


    //local functions:
    fun getFavorites(): Flow<MutableList<UserLocation>> {
        return local.getFavourites()
    }
    suspend fun addToFav(location: UserLocation) {
        local.insertLocation(location)
    }
    suspend fun removeFromFav(location: UserLocation) {
        local.deleteLocation(location)
    }

    //local functions:
    fun getAlerts(): Flow<MutableList<WeatherAlert>> {
        return local.getAlerts()
    }
    suspend fun addAlert(alert:WeatherAlert) {
        local.insertAlert(alert)
    }
    suspend fun removeAlert(alert:WeatherAlert) {
        local.deleteAlert(alert)
    }

    fun getSharedPreference(key:String,value:String):String
    {
        return local.getString(key,value)
    }
    fun putSharedPreference(key:String,value:String)
    {
        return local.putString(key,value)
    }


}

