package com.sotatek.weather.app.home

import androidx.lifecycle.viewModelScope
import com.sotatek.weather.base.BaseViewModel
import com.sotatek.weather.data.remote.dto.WeatherResultDto
import com.sotatek.weather.data.remote.repo.WeatherRepo
import com.sotatek.weather.extension.doIfFailure
import com.sotatek.weather.extension.doIfSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by khiemnt
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo
) : BaseViewModel() {

    private val _weatherResultDto = MutableSharedFlow<WeatherResultDto>()
    val weatherResultDto = _weatherResultDto.asSharedFlow()

    var city: String = ""
    var showTempC = true
    var temp: Int = 0

    fun getWeather() {
        viewModelScope.launch {
            emitLoadingState(true)
            weatherRepo.getWeatherByCity(city).apply {
                doIfSuccess {
                    _weatherResultDto.emit(it)
                }
                doIfFailure {
                    emitError(it)
                }
            }
            emitLoadingState(false)
        }
    }
}