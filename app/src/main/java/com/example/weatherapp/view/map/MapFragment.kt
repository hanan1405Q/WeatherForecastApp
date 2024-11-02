package com.example.weatherapp.view.map

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.R
import com.example.weatherapp.model.local.HelperSharedPreference
import com.example.weatherapp.model.pojo.UserLocation
import com.example.weatherapp.model.remot.RetrofitHelper
import com.example.weatherapp.model.repository.WeatherRepo
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.model.local.LocalDataSource
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var sharedPreferences: HelperSharedPreference
    private lateinit var viewModel: WeatherViewModel
    private lateinit var map: GoogleMap
    private lateinit var selectedLocation: Location
    private val args: MapFragmentArgs by navArgs()

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

        val factory = WeatherViewModelFactory(WeatherRepo(RetrofitHelper, LocalDataSource(requireContext())), LocationServices.getFusedLocationProviderClient(requireContext()))
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        // Call the function to set up autocomplete
        setupAutocomplete()

        binding.mapOptionButton.setOnClickListener{
            // Show the options when the button is clicked
            showMapOptions(it)
        }
        binding.btnDone.setOnClickListener{

        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
       map=googleMap
        observeUserCurrentLocation()
        onMapClicked()

    }

    // Function to display the popup menu
    private fun showMapOptions(view: View) {
        if (!isAdded) return  // Ensure fragment is still attached
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.map_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.normal_map -> {
                    setMapType(GoogleMap.MAP_TYPE_NORMAL)
                    true
                }
                R.id.satellite_map -> {
                    setMapType(GoogleMap.MAP_TYPE_SATELLITE)
                    true
                }
                R.id.terrain_map -> {
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
        if (::map.isInitialized) {
            map.mapType = mapType
        } else {
            Log.e("MapFragment", "GoogleMap not initialized")
        }
    }
    //Function To SetUp the AutoComplete For User Location
    private fun setupAutocomplete() {
        // Initialize the Places SDK
        val api="AIzaSyCCvajuy23ZiG8_iSEx6oWLNNUQFstYNls"
        Places.initialize(requireContext(),api)
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
                place.latLng?.let {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10.0f))
                    map.addMarker(MarkerOptions().position(it))
                    onMapClicked()
                }
            }

            override fun onError(status: Status) {
                // Handle the error
                Log.e("Autocomplete", "Error: ${status.statusMessage}")
            }
        })
    }

    //Observe Current Location of user
    private fun observeUserCurrentLocation() {
        Log.i("HI","Observe User Current Location")
        viewModel.locationLiveData.observe(viewLifecycleOwner) {
            setUserLocation(it.lat, it.lon)
        }
    }

    //Set userLocation On the Map
    private fun setUserLocation(latitude: Double, longitude: Double) {
        Log.i("HI","In Set User Location")
        val location = LatLng(latitude, longitude)
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title("Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
        Log.i("HI","Add marker to current Location")
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
        map.uiSettings.isZoomControlsEnabled = true
    }

    //On Map Clicked (Set SelectedLocation with Location and Showing Save Button )
    private fun onMapClicked() {
        map.setOnMapClickListener { location ->
            map.clear()
            map.addMarker(MarkerOptions().position(location))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
            selectedLocation = Location("")
            selectedLocation.longitude = location.longitude
            selectedLocation.latitude = location.latitude
            binding.btnDone.visibility = View.VISIBLE
        }
    }

    //When Location Selected
    private fun onLocationSelected() {
        if (this::selectedLocation.isInitialized) {
            val location = UserLocation(lat=selectedLocation.latitude, lon = selectedLocation.longitude)
            if (args.isFav) {
                saveFavLocation(location)
            } else {
                sharedPreferences.putString("lat",location.lat.toString())
                sharedPreferences.putString("long",location.lon.toString())
                viewModel.locationLiveData.value = location
            }
        }
        findNavController().popBackStack()
    }

    private fun saveFavLocation(location: UserLocation) {
        viewModel.addToFav(location)
    }


}