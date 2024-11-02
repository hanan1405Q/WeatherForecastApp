package com.example.weatherapp.model.pojo
import android.os.Parcelable

import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherItem>,
    val city: City
) : Parcelable

@Parcelize
data class WeatherItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain? = null,
    val sys: System,
    val dt_txt: String
) : Parcelable

@Parcelize
data class System(
    val pod: String
) : Parcelable

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
) : Parcelable

@Parcelize
data class HourlyForecast(
    val time: String,
    val dt: Long,
    val temperature: Double,
    val weatherDescription: String,
    val weatherIcon: String
) : Parcelable

@Parcelize
data class DailyForecast(
    val date: String,
    val dt: Long,
    val minTemp: Double,
    val maxTemp: Double,
    val weatherDescription: String,
    val weatherIcon: String
) : Parcelable

fun ForecastResponse.toHourlyForecastsForToday(): List<HourlyForecast> {
    val hourlyForecasts = mutableListOf<HourlyForecast>()
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Get all weather items for today
    val todayForecasts = list.filter { weatherItem ->
        val itemDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(weatherItem.dt * 1000))
        itemDate == today
    }

    // Sort by dt to get the correct order of hours
    todayForecasts.sortedBy { it.dt }.take(8).forEach { weatherItem -> // Limiting to 8 hours
        val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = hourFormat.format(Date(weatherItem.dt * 1000))
        hourlyForecasts.add(
            HourlyForecast(
                time = time,
                dt = weatherItem.dt, // Include dt in the HourlyForecast
                temperature = weatherItem.main.temp,
                weatherDescription = weatherItem.weather[0].description,
                weatherIcon = weatherItem.weather[0].icon
            )
        )
    }
    return hourlyForecasts
}

fun ForecastResponse.toDailyForecasts(): List<DailyForecast> {
    val dailyForecasts = mutableListOf<DailyForecast>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Group by date to aggregate daily forecasts
    val dailyMap = list.groupBy { weatherItem ->
        dateFormat.format(Date(weatherItem.dt * 1000))
    }

    dailyMap.entries.take(5).forEach { (date, weatherItems) ->
        val minTemp = weatherItems.minOf { it.main.temp_min }
        val maxTemp = weatherItems.maxOf { it.main.temp_max }
        val weatherDescription = weatherItems[0].weather[0].description
        val weatherIcon = weatherItems[0].weather[0].icon
        val dt = weatherItems[0].dt // Use the dt from the first weather item

        dailyForecasts.add(
            DailyForecast(
                date = date,
                dt = dt, // Include dt in the DailyForecast
                minTemp = minTemp,
                maxTemp = maxTemp,
                weatherDescription = weatherDescription,
                weatherIcon = weatherIcon
            )
        )
    }
    return dailyForecasts
}


