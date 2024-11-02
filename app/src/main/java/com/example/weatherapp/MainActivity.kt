package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val navController = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(binding.navigationBar, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.mapFragment || destination.id == R.id.favoriteDetailsFragment ) {
                binding.navigationBar.visibility = View.GONE
            } else {
                binding.navigationBar.visibility  = View.VISIBLE
            }
        }
    }

}






//    lateinit var txt:TextView
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        txt=findViewById(R.id.txtView)
//
//        val lat="45.133"
//        val lon="7.367"
//        val appid=Utils.API_KEY
//
//        val service=RetrofitHelper.apiServices
//        lifecycleScope.launch(Dispatchers.IO) {
//           var res= service.getCurrentWeather(lat,lon,appid,"metric","en")
//            withContext(Dispatchers.Main)
//            {
//                Snackbar.make(this@MainActivity.txt,"Get Weather ${res.body()?.main?.temp ?: "N/A"}",Snackbar.LENGTH_LONG).show()
//            }
//
//        }
//    }