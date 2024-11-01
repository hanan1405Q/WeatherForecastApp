package com.example.weatherapp.ui.map

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.R
import com.example.weatherapp.data.local.HelperSharedPreference
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var sharedPreferences: HelperSharedPreference
    private lateinit var viewModel: WeatherViewModel
    private lateinit var map: GoogleMap
    private lateinit var selectedLocation: Location
    //private val args: MapFragmentArgs by navArgs()

    private lateinit var binding: FragmentMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.mapOptionButton.setOnClickListener{
            // Show the options when the button is clicked
            showMapOptions(it)
        }

        // Initialize the Places SDK
        val api=getString(R.string.Map_API_KEY)
        Places.initialize(requireContext(),api)
        // Call the function to set up autocomplete
        setupAutocomplete()

    }

    override fun onMapReady(googleMap: GoogleMap) {
       map=googleMap
    }

    // Function to display the popup menu
    private fun showMapOptions(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.map_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.normal_map -> {
                    // Handle Normal Map option
                    setMapType(GoogleMap.MAP_TYPE_NORMAL)
                    true
                }
                R.id.satellite_map -> {
                    // Handle Satellite Map option
                    setMapType(GoogleMap.MAP_TYPE_SATELLITE)
                    true
                }
                R.id.terrain_map -> {
                    // Handle Terrain Map option
                    setMapType(GoogleMap.MAP_TYPE_TERRAIN)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    // Function to change the map type
    private fun setMapType(mapType: Int) {
        // Ensure GoogleMap object is initialized (e.g., from onMapReady)
        if (::map.isInitialized) {
            map.mapType = mapType
        } else {
            Log.e("MapFragment", "GoogleMap not initialized")
        }
    }

    private fun setupAutocomplete() {
        // Retrieve the AutocompleteSupportFragment
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of data to return
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )

        // Set up the listener for place selection
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Handle the selected place
                Log.i("Autocomplete", "Place: ${place.name}, ${place.address}, ${place.latLng}")
                Toast.makeText(
                    requireContext(),
                    "Selected: ${place.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(status: Status) {
                // Handle the error
                Log.e("Autocomplete", "Error: ${status.statusMessage}")
                Toast.makeText(
                    requireContext(),
                    "Error: ${status.statusMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}