package dev.cherran.weatherapp

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}

data class Forecast(val maxTemp: Float, val minTemp: Float, val humidity: Float, val description: String, val icon: Int) {

}