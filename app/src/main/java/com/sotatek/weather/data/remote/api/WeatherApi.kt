package com.sotatek.weather.data.remote.api

import com.sotatek.weather.data.remote.dto.ForecastResultDto
import com.sotatek.weather.data.remote.dto.WeatherResultDto
import com.sotatek.weather.util.Constant
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather?appid=${Constant.WEATHER_API_KEY}&units=metric")
    suspend fun getCurrentWeather(@Query("q") city:String): WeatherResultDto

    @GET("data/2.5/forecast?appid=${Constant.WEATHER_API_KEY}&units=metric")
    suspend fun getForecast(@Query("q") city:String): ForecastResultDto

}