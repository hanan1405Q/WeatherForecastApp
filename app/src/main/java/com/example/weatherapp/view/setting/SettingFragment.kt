package com.example.weatherapp.view.setting

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.model.local.HelperSharedPreference
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.util.SnackbarUtils
import com.example.weatherapp.util.Utils.setLocale

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private  lateinit var sharedPreferences:HelperSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=HelperSharedPreference(requireContext())
        setLocale(sharedPreferences.getString("language","en"),requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Load saved settings when the view is created
        loadSettings()
        // Save button click listener
        binding.btnSave.setOnClickListener {
            saveSettings()
            setLocale(sharedPreferences.getString("language","en"),requireContext())
        }
    }

    // Save the selected RadioGroup options to SharedPreferences
    private fun saveSettings() {
        val selectedLanguage = when (binding.radioGroupLang.checkedRadioButtonId) {
            R.id.radio_arabic -> "ar"
            R.id.radio_english -> "en"
            else -> "en" // Default to English
        }

        val selectedUnit = when (binding.radioGroupUnits.checkedRadioButtonId) {
            R.id.radio_cms-> "metric"
            R.id.radio_fmh -> "imperial"
            R.id.radio_kms->"standard"
            else -> "metric" // Default to Metric
        }
        val selectedLocationMethod = when (binding.radioGroupLoc.checkedRadioButtonId) {
            R.id.radio_maps-> "map"
            R.id.radio_gps -> "gps"
            else -> "gps" // Default to Metric
        }

        // Save to SharedPreferences
        with(sharedPreferences) {
            putString("language", selectedLanguage)
            putString("units", selectedUnit)
            putString("location", selectedLocationMethod)
        }
        SnackbarUtils.showSnackbar(binding.root, "Settings saved", Color.GREEN)
    }

    // Load the saved settings from SharedPreferences and set the UI state
    private fun loadSettings() {
        val savedLanguage = sharedPreferences.getString("language", "en")
        val savedUnit = sharedPreferences.getString("units", "metric")
        val savedLocation = sharedPreferences.getString("location", "gps")
        // Restore the correct radio button selection for language
        when (savedLanguage) {
            "ar" -> binding.radioGroupLang.check(R.id.radio_arabic)
            "en" -> binding.radioGroupLang.check(R.id.radio_english)
        }

        // Restore the correct radio button selection for units
        when (savedUnit) {
            "metric" -> binding.radioGroupUnits.check(R.id.radio_cms)
            "imperial" -> binding.radioGroupUnits.check(R.id.radio_fmh)
            "standard" -> binding.radioGroupUnits.check(R.id.radio_kms)
        }

        when (savedLocation) {
            "map" -> binding.radioGroupLoc.check(R.id.radio_setting_map)
            "gps" -> binding.radioGroupLoc.check(R.id.radio_setting_gps)

        }
    }

}