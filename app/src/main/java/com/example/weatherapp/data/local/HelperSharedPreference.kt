package com.example.weatherapp.data.local

import android.content.Context

class HelperSharedPreference constructor(private val context: Context){
    private val sharedPreferences = context.getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE)

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}