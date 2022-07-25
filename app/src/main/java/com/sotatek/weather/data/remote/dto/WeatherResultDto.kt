package com.sotatek.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResultDto(
    @SerializedName("weather")
    val weather: List<WeatherDto>,

    @SerializedName("main")
    val weatherBasicDto: WeatherBasicDto,

    @SerializedName("dt_txt")
    val dateTime: String
)

