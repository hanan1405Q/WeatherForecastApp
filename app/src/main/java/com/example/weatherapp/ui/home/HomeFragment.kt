package com.example.weatherapp.ui.home

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.local.HelperSharedPreference
import com.example.weatherapp.data.pojo.CurrentWeatherResponse
import com.example.weatherapp.data.remot.RetrofitHelper
import com.example.weatherapp.data.repository.WeatherRepo
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.ui.home.adapters.DailyAdapter
import com.example.weatherapp.ui.home.adapters.HourlyAdapter
import com.example.weatherapp.util.Utils
import com.example.weatherapp.util.Utils.getTemperatureUnit
import com.example.weatherapp.util.Utils.setLocale
import com.example.weatherapp.viewmodel.ApiState
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Calendar


class HomeFragment : Fragment() {

    val TAG = "HomeFragment"
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit  var fusedClient: FusedLocationProviderClient

//    private lateinit var hourlyAdapter:HourlyAdapter
//    private lateinit var dailyAdapter :DailyAdapter
    private val hourlyAdapter by lazy { HourlyAdapter() }
    private val dailyAdapter by lazy { DailyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(getLanguage(), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = WeatherViewModelFactory(WeatherRepo(RetrofitHelper), LocationServices.getFusedLocationProviderClient(requireContext()))
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        setupDailyRecyclerView()
        setupHourlyRecyclerView()
        getLastLocation()
        observeLocationChange()
        observeWeatherResponse()


    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeWeatherResponse()
    {
        lifecycleScope.launch {

            launch {
                viewModel.weatherState.collect { state ->
                    when (state) {
                        is ApiState.Loading -> {
                            // Show loading indicator
                            showShimmer()
                        }

                        is ApiState.Success -> {
                            hideShimmer()
                            val weather = state.data.body()
                            weather?.let { setUi(it) }
                        }

                        is ApiState.Failure -> {
                            hideShimmer()
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            launch {
                viewModel.hourlyForecastState.collect { state ->
                    when (state) {
                        is ApiState.Loading -> { showShimmer() }
                        is ApiState.Success -> {
                            hideShimmer()
                            hourlyAdapter.submitList(state.data)
                            Log.i("VM","Submit"+state.data.get(0).dt.toString())
                        }
                        is ApiState.Failure -> {
                            hideShimmer()
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            launch {
                viewModel.dailyForecastState.collect { state ->
                    when (state) {
                        is ApiState.Loading -> { showShimmer() }
                        is ApiState.Success -> {
                            hideShimmer()
                            dailyAdapter.submitList(state.data)
                        }
                        is ApiState.Failure -> {
                            hideShimmer()
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }


        }
    }


    private fun checkPermission() = ActivityCompat.checkSelfPermission(
        requireActivity(),
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
private val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            Log.d(TAG, "Permissions granted")
            getLastLocation()
        } else {
            Log.d(TAG, "Permissions denied")
            hideAllViews()
            showAlertDialog()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private fun observeLocationChange() {
        viewModel.locationLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: location is here")

            viewModel.fetchCurrentWeather(
                it.lat,
                it.lon,
                getUnits(),
                getLanguage()
            )

            viewModel.fetchForecastWeather(
                it.lat,
                it.lon,
                getUnits(),
                getLanguage()
            )


        }
    }


    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                viewModel.requestNewLocationData()
//                if (sharedPreferences.getBoolean(Utils.IS_MAP, false)) {
//                    findNavController().navigate(
//                        HomeFragmentDirections.actionHomeFragmentToMapFragment(
//                            false
//                        )
//                    )}

            } else {
                enableLocationSetting()
            }
        } else {
            requestPermission()
        }
    }
    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage(getString(R.string.ask_permission))
        alertDialogBuilder.setPositiveButton(getString(R.string.permission_postive_button)) { dialog: DialogInterface, _: Int ->
            gotoAppPermission()
        }
        alertDialogBuilder.setNegativeButton(resources.getString(R.string.cancel)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun gotoAppPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun enableLocationSetting() {
        Toast.makeText(requireActivity(), "Turn on Location", Toast.LENGTH_SHORT).show()
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity?.startActivity(settingIntent)
    }

    private fun setupHourlyRecyclerView() {
        binding.rvHourly.apply {
            adapter = hourlyAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupDailyRecyclerView() {
        binding.rvDaily.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /***************       UI                     *******************/
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUi(weatherResponse: CurrentWeatherResponse) {

        val backgroundImage = getBackgroundImage(weatherResponse.weather.get(0).main,isNight())
        // Set the background to the NestedScrollView
        binding.nestedScrollView.setBackgroundResource(backgroundImage)

        binding.apply {


            weatherResponse.dt?.let { date ->
                val timezoneOffset = weatherResponse.timezone ?: 0
                val formattedDate = Utils.convertUnixToDate(date, timezoneOffset)
                tvDate.text = formattedDate
            }


            weatherResponse.weather?.get(0)?.let { weather ->
                tvWeatherDesc.text = weather.description
                Glide
                    .with(binding.root)
                    .load("https://openweathermap.org/img/wn/${weather.icon}@2x.png")
                    .into(ivWeather)
            }

            val tempFormat = getString(
                R.string.temp_deg,
                weatherResponse.main.temp?.toInt(),
                getTemperatureUnit(requireContext())
            )
           tvTemp.text = tempFormat

            val pressureFormat = getString(
                R.string.pressure_deg,
                weatherResponse.main?.pressure,
                requireContext().getString(R.string.hpa)
            )
           tvPressureDeg.text = pressureFormat

            val windFormat: String = getString(
                R.string.wind_deg,
                weatherResponse.wind?.deg,
                Utils.getSpeedUnit(requireContext())
            )
            tvWindDeg.text = windFormat

            val humidityFormat = getString(
                R.string.humidity_deg,
                weatherResponse.main?.humidity,
                "%"
            )
           tvHumidityDeg.text = humidityFormat

            val cloudFormat = getString(
                R.string.cloud_deg,
                weatherResponse.clouds.all,
                "%"
            )
            tvCloudDeg.text = cloudFormat
            val uvFormat = getString(
                R.string.uv_deg,
                weatherResponse.wind.gust?.toInt(),
                " "
            )
            tvRayDeg.text = uvFormat

            val visibilityFormat = getString(
                R.string.visibility_deg,
                weatherResponse.visibility,
                " "
            )
            tvVisibilityDeg.text = visibilityFormat

            tvAddress.text = Utils.getAddress(
                requireContext(),
                weatherResponse.coord.lat ?: 0.0,
                weatherResponse.coord.lon ?: 0.0
            )
        }


    }

    private fun hideAllViews() {
        binding.apply {
            tvAddress.visibility = View.GONE
            tvDate.visibility = View.GONE
            llWeatherCard.visibility = View.GONE
            shimmerView.visibility = View.GONE
            rvHourly.visibility = View.GONE
            rvDaily.visibility = View.GONE
        }
    }

    private fun showShimmer() {
        binding.apply {
            tvAddress.visibility = View.GONE
            tvDate.visibility = View.GONE
            llWeatherCard.visibility = View.GONE
            rvHourly.visibility = View.GONE
            rvDaily.visibility = View.GONE
            shimmerView.visibility = View.VISIBLE

            shimmerView.startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.apply {
            shimmerView.stopShimmer()
            shimmerView.visibility = View.GONE
            llWeatherCard.visibility = View.VISIBLE
            tvAddress.visibility = View.VISIBLE
            tvDate.visibility = View.VISIBLE
            rvHourly.visibility = View.VISIBLE
            rvDaily.visibility = View.VISIBLE
        }
    }


    fun isNight(): Boolean {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) // Get current hour (0-23)
        return currentHour >= 17 || currentHour < 6  // Check if it's between 6 PM and 6 AM
    }

    fun getBackgroundImage(condition: String, isNight: Boolean): Int {
        return if (isNight) {
            R.drawable.night_bg
        } else {
            when (condition) {
                "Clear" -> R.drawable.sunny_bg
                "Clouds" -> R.drawable.cloudy_bg
                "Rain" -> R.drawable.rainy_bg
                "Snow" -> R.drawable.snow_bg
                "Thunderstorm" -> R.drawable.haze_bg
                "Drizzle", "Mist", "Fog", "Haze" -> R.drawable.haze_bg
                else -> R.drawable.background_2 // Fallback image
            }
        }
    }


    fun getUnits():String
    {
        val sharedPref=HelperSharedPreference(requireContext())
        return sharedPref.getString("units","metric")
    }
    fun getLanguage():String
    {
        val sharedPref=HelperSharedPreference(requireContext())
        return sharedPref.getString("language","en")
    }


}



