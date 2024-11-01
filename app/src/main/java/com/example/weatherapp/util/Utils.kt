package com.example.weatherapp.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Geocoder
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import com.example.weatherapp.R
import com.example.weatherapp.data.local.HelperSharedPreference
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Utils {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY ="9478d6e1cb99ea0d4ecfab94c91c2298"


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUnixToDate(dt: Long, timezoneOffset: Int): String {
        // Convert UNIX timestamp to an Instant
        val instant = Instant.ofEpochSecond(dt)
        // Convert Instant to LocalDateTime with the provided timezone offset
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofTotalSeconds(timezoneOffset))
        // Define the format you want (e.g., "dd-MM-yyyy HH:mm:ss")
        val formatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
        // Format the LocalDateTime to a string
        return localDateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayNameFromDate(dateString: String): String {
        // Define the date format to match the input string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // Parse the string to LocalDate
        val date = LocalDate.parse(dateString, formatter)
        // Get the DayOfWeek
        val dayOfWeek: DayOfWeek = date.dayOfWeek
        // Return the name of the day (in full, e.g., "MONDAY")
        return dayOfWeek.name // For full name; use dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH) for localized names
    }


    fun setLocale(language: String, context: Context) {
        val myLocale = Locale(language)
        Locale.setDefault(myLocale)
        val res: Resources = context.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }


    fun convertLongToDayName(time: Long): String {
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        return format.format(Date(time * 1000))
    }

    fun convertLongToTime(time: Long): String {
        val format = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        return format.format(Date(time * 1000))
    }


//
//    fun getDateMillis(date: String): Long {
//        val f = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val d: Date = f.parse(date)
//        return d.time
//    }
//
//    fun convertLongToDayDateAlert(time: Long): String {
//        val date = Date(time)
//        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        return format.format(date)
//    }
//
//    fun convertLongToTimePicker(time: Long): String {
//        val date = Date(time - 7200000)
//        val format = SimpleDateFormat("h:mm aa", Locale.getDefault())
//        return format.format(date)
//    }
//
    fun getSpeedUnit(context: Context): String {
        val sharedPreference = HelperSharedPreference(context)
        return when (sharedPreference.getString("units", "metric")) {
            "imperial" -> {
                context.getString(R.string.m_h)
            }
            else -> {
                context.getString(R.string.m_s)
            }
        }

    }

    fun getTemperatureUnit(context: Context): String {
        val sharedPreference = HelperSharedPreference(context)
        return when (sharedPreference.getString("units", "metric")) {
            "imperial" -> {
                context.getString(R.string.f)
            }
            "standard" -> {
                context.getString(R.string.k)
            }
            else -> {
                context.getString(R.string.c)
            }
        }

    }

    fun getAddress(context: Context, lat: Double, lon: Double): String {
        var address = " "
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(lat, lon, 1)
            if (addressList != null) {
                if (addressList.isNotEmpty()) {
                    address = "${addressList[0].adminArea}, ${addressList[0].countryName}"
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return address
    }
}