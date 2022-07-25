package com.sotatek.weather.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sotatek.weather.data.remote.api.WeatherApi
import com.sotatek.weather.data.remote.errors.JsonConverter
import com.sotatek.weather.data.remote.errors.JsonConverterImpl
import com.sotatek.weather.data.remote.repo.WeatherRepo
import com.sotatek.weather.data.remote.service.WeatherService
import com.sotatek.weather.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val REQUEST_TIMEOUT = 30L

    @Provides
    fun provideBaseUrl() = Constant.BASE_URL

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @Provides
    @Singleton
    fun provideJsonConverter(gson: Gson): JsonConverter = JsonConverterImpl(gson)

    @Singleton
    @Provides
    fun provideRepository(
        weatherService: WeatherService
    ) = WeatherRepo(weatherService)

}