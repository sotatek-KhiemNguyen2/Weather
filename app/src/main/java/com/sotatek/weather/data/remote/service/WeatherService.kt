package com.sotatek.weather.data.remote.service

import com.sotatek.weather.base.BaseService
import com.sotatek.weather.data.remote.api.WeatherApi
import com.sotatek.weather.data.remote.errors.StandardError
import javax.inject.Inject

class WeatherService @Inject constructor(private val weatherApi: WeatherApi) : BaseService() {
    suspend fun getWeatherByCity(city:String) = eitherNetwork(StandardError::class) {
        weatherApi.getCurrentWeather(city)
    }

    suspend fun getForecastByCity(city:String) = eitherNetwork(StandardError::class) {
        weatherApi.getForecast(city)
    }
}