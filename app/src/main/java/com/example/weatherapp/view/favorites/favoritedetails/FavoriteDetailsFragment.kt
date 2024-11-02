package com.example.weatherapp.view.favorites.favoritedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.databinding.FragmentFavoriteDetailsBinding


class FavoriteDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFavoriteDetailsBinding.inflate(inflater,container,false)
        return  binding.root
    }

}