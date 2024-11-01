package com.example.weatherapp.data.pojo
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain? = null,  // Nullable fields should have default values
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
):Parcelable

@Parcelize
data class Coord(
    val lon: Double,
    val lat: Double
):Parcelable

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
):Parcelable


@Parcelize
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int? = null,  // Nullable fields require default values
    val grnd_level: Int? = null
):Parcelable

@Parcelize
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
):Parcelable

@Parcelize
data class Rain(
    val `1h`: Double? = null  // Use backticks to escape invalid field names
):Parcelable

@Parcelize
data class Clouds(
    val all: Int
):Parcelable

@Parcelize
data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
):Parcelable

