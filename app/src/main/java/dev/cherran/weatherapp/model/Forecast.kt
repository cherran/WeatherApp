package dev.cherran.weatherapp.model

import java.io.Serializable

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}



data class Forecast(private val maxTemp: Float, private val minTemp: Float, val humidity: Float, val description: String, val icon: Int): Serializable {

    init {
        if (humidity !in 0f..100f) {
            throw IllegalArgumentException("Humidity shoud be between 0f and 100f")
        }
    }

    protected fun toFahrenheit(celsius: Float) = celsius * 1.8f + 32

    fun getMaxTemp(units: TemperatureUnit) = when (units) {
        TemperatureUnit.CELSIUS -> maxTemp // Los datos se guardarán en celsius
        TemperatureUnit.FAHRENHEIT -> toFahrenheit(maxTemp)
    }

    fun getMinTemp(units: TemperatureUnit) = when (units) {
        TemperatureUnit.CELSIUS -> minTemp // Los datos se guardarán en celsius
        TemperatureUnit.FAHRENHEIT -> toFahrenheit(minTemp)
    }
}
