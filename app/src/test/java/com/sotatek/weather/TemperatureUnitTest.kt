package com.sotatek.weather

import com.sotatek.weather.extension.getIconUrl
import com.sotatek.weather.extension.toFahrenheit
import org.junit.Test
import org.junit.Assert.*

class TemperatureUnitTest {
    //test convert Celsius to Fahrenheit
    @Test
    fun `toFahrenheit() Test`() {
        assertEquals(50, 10.toFahrenheit())
        assertEquals(71, 22.toFahrenheit())
        assertEquals(212, 100.toFahrenheit())
    }

    @Test
    fun `getIconUrl() Test`() {
        val iconName = "10d"
        val expectUrl = "https://openweathermap.org/img/wn/10d@2x.png"
        assertEquals(expectUrl, getIconUrl(iconName))
    }
}