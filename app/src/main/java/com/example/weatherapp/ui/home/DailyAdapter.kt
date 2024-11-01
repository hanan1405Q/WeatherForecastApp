package com.example.weatherapp.ui.home.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.pojo.DailyForecast
import com.example.weatherapp.databinding.ItemDailyBinding
import com.example.weatherapp.util.Utils
import com.example.weatherapp.util.Utils.getTemperatureUnit

class DailyAdapter :
    ListAdapter<DailyForecast, DailyAdapter.MyViewHolder>(
        DailyDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)

        fun bind(daily: DailyForecast) {
            binding.apply {
                daily.dt?.let {
                    tvDay.text = Utils.convertLongToDayName(it)
                }
                tvDayStatus.text = daily.weatherDescription ?: ""

                val strFormat: String = binding.root.context.getString(
                    R.string.daily,
                    daily.maxTemp.toInt(),
                    daily.minTemp.toInt(),
                    getTemperatureUnit(binding.root.context)
                )
                tvMaxMinTemp.text = strFormat

                Glide
                    .with(binding.root)
                    .load("https://openweathermap.org/img/wn/${daily.weatherIcon?: ""}.png?fbclid=IwAR2Nk0UQ5anrxUCLubc6bRZTqN65qD2TE2Rk0EvU6-609jRf_HuHPAnP-YE")
                    .into(ivDayDesc)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDailyBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    class DailyDiffCallback : DiffUtil.ItemCallback<DailyForecast>() {
        override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem.dt== newItem.dt
        }

        override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem == newItem
        }
    }
}