package com.example.weatherapp.data.remot


import com.example.weatherapp.data.pojo.CurrentWeatherResponse
import com.example.weatherapp.data.pojo.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") lat:Double,
                                  @Query("lon") lon:Double,
                                  @Query("units") units:String,
                                  @Query("lang") lang:String,
                                  @Query("appid") key:String):Response<CurrentWeatherResponse>

    @GET("forecast")
    suspend fun getForecastWeather(@Query("lat") lat:Double,
                                  @Query("lon") lon:Double,
                                  @Query("units") units:String,
                                  @Query("lang") lang:String,
                                  @Query("appid") key:String):ForecastResponse



}