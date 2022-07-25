package com.sotatek.weather.app.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sotatek.weather.R
import com.sotatek.weather.data.remote.dto.WeatherResultDto
import com.sotatek.weather.databinding.WeatherItemBinding
import com.sotatek.weather.extension.getIconUrl
import com.sotatek.weather.extension.toFahrenheit

class ForecastAdapter : ListAdapter<WeatherResultDto, ForecastAdapter.WeatherViewHolder>(WeatherComparator()) {
    var showTempC = true
    var city = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class WeatherViewHolder(private val binding: WeatherItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherResultDto: WeatherResultDto) {
            binding.apply {
                val weather = weatherResultDto.weather[0]
                val icon = weather.icon
                Glide.with(itemView)
                    .load(getIconUrl(icon))
                    .into(imgIcon)

                val weatherBasicDto = weatherResultDto.weatherBasicDto
                val temp = weatherBasicDto.temp.toInt()
                textHumidity.text = String.format(context.getString(R.string.humidity), "${weatherBasicDto.humidity.toInt()} %")
                textDescription.text = String.format(context.getString(R.string.description_weather), weather.main, weather.description)
                when (showTempC) {
                    true -> {
                        textTemperature.text = temp.toString()
                        textTempC.text = context.getString(R.string.temp_c_without_line)
                        textTempC.isVisible = true
                        textTempF.isVisible = false
                    }
                    else -> {
                        textTemperature.text = temp.toFahrenheit().toString()
                        textTempF.text = context.getString(R.string.temp_f)
                        textTempC.isVisible = false
                        textTempF.isVisible = true
                    }
                }

                textDatetime.text = weatherResultDto.dateTime
                textCountry.text = city

                textHumidity.isVisible = true
                textDescription.isVisible = true
                textDatetime.isVisible = true
                textCountry.isVisible = true
            }
        }
    }

    class WeatherComparator : DiffUtil.ItemCallback<WeatherResultDto>() {
        override fun areItemsTheSame(oldItem: WeatherResultDto, newItem: WeatherResultDto) =
            oldItem.dateTime == newItem.dateTime

        override fun areContentsTheSame(oldItem: WeatherResultDto, newItem: WeatherResultDto) =
            oldItem == newItem
    }
}