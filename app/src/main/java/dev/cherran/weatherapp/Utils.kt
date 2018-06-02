package dev.cherran.weatherapp

import android.content.Context
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.model.TemperatureUnit

fun units2String(units: TemperatureUnit) = if (units == TemperatureUnit.CELSIUS) "ºC"
else "F"

fun forecastDay(context: Context, index: Int) = when(index) {
    0 -> context.getString(R.string.today)
    1 -> context.getString(R.string.tomorrow)
    2 -> context.getString(R.string.day_after_tomorrow)
    3 -> context.getString(R.string.day_after_after_tomorrow)
    4 -> context.getString(R.string.day_after_after_after_tomorrow)
    else -> context.getString(R.string.unknown_day)
}