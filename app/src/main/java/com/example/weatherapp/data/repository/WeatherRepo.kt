package com.example.weatherapp.data.repository

import com.example.weatherapp.data.pojo.CurrentWeatherResponse
import com.example.weatherapp.data.pojo.DailyForecast
import com.example.weatherapp.data.pojo.ForecastResponse
import com.example.weatherapp.data.pojo.HourlyForecast
import com.example.weatherapp.data.pojo.toDailyForecasts
import com.example.weatherapp.data.pojo.toHourlyForecastsForToday
import com.example.weatherapp.data.remot.RetrofitHelper
import com.example.weatherapp.data.remot.WeatherService
import com.example.weatherapp.util.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRepo (val remote: RetrofitHelper){

    companion object{
        @Volatile
        private var repository:WeatherRepo? = null
        fun getInstance(retrofitHelper:RetrofitHelper):WeatherRepo{
            return repository ?: synchronized(this){
                val temp =  WeatherRepo(retrofitHelper)
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

//    // Flow to emit hourly forecast for today
//    suspend fun getHourlyForecastForToday(
//        lat: Double, long: Double, unit: String, lang: String
//    ): Flow<List<HourlyForecast>> = flow {
//        fetchForecast(lat, long, unit, lang).collect { forecastResponse ->
//            emit(forecastResponse.toHourlyForecastsForToday())
//        }
//    }
//
//    // Flow to emit daily forecast for the next 5 days
//   suspend fun getDailyForecastForNext5Days(
//        lat: Double, long: Double, unit: String, lang: String
//    ): Flow<List<DailyForecast>> = flow {
//        fetchForecast(lat, long, unit, lang).collect { forecastResponse ->
//            emit(forecastResponse.toDailyForecasts())
//        }
//    }
}

