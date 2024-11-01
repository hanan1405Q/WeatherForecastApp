package com.example.weatherapp.viewmodel

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.pojo.CurrentWeatherResponse
import com.example.weatherapp.data.pojo.DailyForecast
import com.example.weatherapp.data.pojo.HourlyForecast
import com.example.weatherapp.data.pojo.UserLocation
import com.example.weatherapp.data.pojo.toDailyForecasts
import com.example.weatherapp.data.pojo.toHourlyForecastsForToday
import com.example.weatherapp.data.repository.WeatherRepo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel(private val _repo:WeatherRepo,private val mFusedLocationProviderClient:FusedLocationProviderClient):
    ViewModel() {

    val locationLiveData: MutableLiveData<UserLocation> = MutableLiveData()

    // StateFlow to hold the current weather data
    private val _weatherState = MutableStateFlow<ApiState<Response<CurrentWeatherResponse>>>(ApiState.Loading())
    val weatherState: StateFlow<ApiState<Response<CurrentWeatherResponse>>> = _weatherState

    // StateFlow for hourly forecast
    private val _hourlyForecastState = MutableStateFlow<ApiState<List<HourlyForecast>>>(ApiState.Loading())
    val hourlyForecastState: StateFlow<ApiState<List<HourlyForecast>>> = _hourlyForecastState

    // StateFlow for daily forecast
    private val _dailyForecastState = MutableStateFlow<ApiState<List<DailyForecast>>>(ApiState.Loading())
    val dailyForecastState: StateFlow<ApiState<List<DailyForecast>>> = _dailyForecastState


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val myLocation = locationResult.lastLocation
            myLocation?.let {
                val location = UserLocation(it.latitude,it.longitude)
                locationLiveData.value = location
            }
            stopLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
        Log.d("SharedViewModel", "requestNewLocationData is here")
        val mLocationRequest = LocationRequest().apply {
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            setInterval(0)
        }

        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }
    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    // Fetch the current weather and update state
    fun fetchCurrentWeather(lat: Double, long: Double, unit: String, lang: String) {
        viewModelScope.launch {
            _repo.getCurrentWeather(lat, long, unit, lang)
                .catch { e ->
                    // Handle the error (e.g., log it, show error state)
                    _weatherState.value = ApiState.Failure(e)
                }
                .collectLatest { weather ->
                    _weatherState.value = ApiState.Success(weather)
                }
        }
    }

    // Fetch the current weather and update state
    fun fetchForecastWeather(lat: Double, long: Double, unit: String, lang: String) {
        viewModelScope.launch {
            _repo.getForecastWeather(lat, long, unit, lang)
                .catch { e ->
                    // Update both states with failure if there's an error
                    _dailyForecastState.value = ApiState.Failure(e)
                    _hourlyForecastState.value = ApiState.Failure(e)
                }
                .collectLatest {  forecastResponse ->
                    // Update daily forecast state
                    val dailyForecasts = forecastResponse.toDailyForecasts() // Transform into daily forecast list
                    _dailyForecastState.value = ApiState.Success(dailyForecasts)
                    Log.i("VM","Collect Daily"+dailyForecasts.get(0).dt.toString())

                    // Update hourly forecast state
                    val hourlyForecasts = forecastResponse.toHourlyForecastsForToday() // Transform into hourly forecast list
                    _hourlyForecastState.value = ApiState.Success(hourlyForecasts)
                    Log.i("VM","Collect Hourly"+hourlyForecasts.get(0).dt.toString())

                }
        }
    }

}




/*
* ..Act as a Recipe to create object from WeatherViewModel through ViewModelProvider
* ..In Cas The ViewModel has an Parameter in the constructor
*/
class WeatherViewModelFactory(private val _repo:WeatherRepo,private val mFused:FusedLocationProviderClient):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(_repo,mFused) as T
    }
}




sealed class ApiState<T> {
    class Success<T>(val data: T) : ApiState<T>()
    class Failure<T>(val msg: Throwable) : ApiState<T>()
    class Loading<T> : ApiState<T>()
}
