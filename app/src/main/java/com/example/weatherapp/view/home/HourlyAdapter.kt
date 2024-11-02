package com.example.weatherapp.view.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.pojo.HourlyForecast
import com.example.weatherapp.databinding.ItemHourlyBinding
import com.example.weatherapp.util.Utils
import com.example.weatherapp.util.Utils.getTemperatureUnit


class HourlyAdapter :
    ListAdapter<HourlyForecast, HourlyAdapter.MyViewHolder>(
        HourlyDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hourly: HourlyForecast) {
            binding.apply {
                tvHour.text =Utils.convertLongToTime(hourly.dt)
                val strFormat: String = binding.root.context.getString(
                    R.string.hourly,
                    hourly.temperature.toInt(),
                    getTemperatureUnit(binding.root.context)
                )
                tvTemp.text = strFormat

                Glide
                    .with(binding.root)
                    .load("https://openweathermap.org/img/wn/${hourly.weatherIcon?: ""}.png?fbclid=IwAR2Nk0UQ5anrxUCLubc6bRZTqN65qD2TE2Rk0EvU6-609jRf_HuHPAnP-YE")
                    .into(ivHourDesc)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHourlyBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    class HourlyDiffCallback : DiffUtil.ItemCallback<HourlyForecast>() {
        override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem == newItem
        }
    }
}