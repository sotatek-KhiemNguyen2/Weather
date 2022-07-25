package com.sotatek.weather.data.remote.repo

import com.sotatek.weather.data.remote.service.WeatherService

class WeatherRepo(
    private val weatherService: WeatherService
) {
    suspend fun getWeatherByCity(city: String) = weatherService.getWeatherByCity(city)

    suspend fun getForecastByCity(city: String) = weatherService.getForecastByCity(city)
}