package com.sotatek.weather.app.forecast

import androidx.lifecycle.viewModelScope
import com.sotatek.weather.base.BaseViewModel
import com.sotatek.weather.data.remote.dto.ForecastResultDto
import com.sotatek.weather.data.remote.dto.WeatherResultDto
import com.sotatek.weather.data.remote.repo.WeatherRepo
import com.sotatek.weather.extension.DateUtils
import com.sotatek.weather.extension.doIfFailure
import com.sotatek.weather.extension.doIfSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by khiemnt
 */

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo
) : BaseViewModel() {

    private val _forecastResultDto = MutableSharedFlow<ForecastResultDto>()
    val forecastResultDto = _forecastResultDto.asSharedFlow()

    var showTempC = true

    fun getForecast(city: String) {
        if (city.isBlank()) return
        viewModelScope.launch {
            emitLoadingState(true)
            weatherRepo.getForecastByCity(city).apply {
                doIfSuccess {
                    _forecastResultDto.emit(it)
                }
                doIfFailure {
                    emitError(it)
                }
            }
            emitLoadingState(false)
        }
    }

    fun getWholeSaleForecast(weatherResultDtoList: List<WeatherResultDto>): List<WeatherResultDto> {
        val currentDate = DateUtils.toIsoDateUTC(Date())
        val result = weatherResultDtoList.filter {
            it.dateTime.contains(currentDate)
        }.toMutableList()
        return result
    }
}