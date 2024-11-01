package com.example.weatherapp.data.remot

import com.example.weatherapp.util.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit = Retrofit.Builder()
                           .baseUrl(Utils.BASE_URL)
                           .addConverterFactory(GsonConverterFactory.create())
                           .build()
    val apiServices= retrofit.create(WeatherService::class.java)
}