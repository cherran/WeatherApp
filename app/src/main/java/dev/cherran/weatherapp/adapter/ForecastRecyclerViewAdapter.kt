package dev.cherran.weatherapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.forecastDay
import dev.cherran.weatherapp.getTemperatureUnits
import dev.cherran.weatherapp.model.Forecast
import dev.cherran.weatherapp.model.TemperatureUnit
import dev.cherran.weatherapp.units2String


class ForecastRecyclerViewAdapter(private val forecast: List<Forecast>): RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder>() {

    var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_forecast, parent, false)
        // Le decimos a este view que cuando lo pulsen avise a nuestro onClickListener
        view.setOnClickListener {
            onClickListener?.onClick(it)
        }

        return ForecastViewHolder(view)
    }

    override fun getItemCount() = forecast.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) { // ~= cellForRowAt
        holder.bindForecast(forecast[position], getTemperatureUnits(holder.itemView.context), position)
    }

    inner class ForecastViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { // itemView --> en este caso la CardView
        val dayText = itemView.findViewById<TextView?>(R.id.day)
        val forecastImage = itemView.findViewById<ImageView?>(R.id.forecast_image)
        val maxTemp = itemView.findViewById<TextView?>(R.id.max_temp)
        val minTemp = itemView.findViewById<TextView?>(R.id.min_temp)
        val humidity = itemView.findViewById<TextView?>(R.id.humidity)
        val forecastDescription = itemView.findViewById<TextView?>(R.id.forecast_description)
        val context = itemView.context

        fun bindForecast(forecast: Forecast, temperatureUnit: TemperatureUnit, day: Int) { // nombre típico: bindModelo
            // Actualizamos la vista con el modelo
            dayText?.text = forecastDay(context, day)

            forecastImage?.setImageResource(forecast.icon)
            forecastDescription?.text = forecast.description

            updateTemperatureView(forecast, temperatureUnit)
            humidity?.text = context.getString(R.string.humidity_format, forecast.humidity)
        }

        fun updateTemperatureView(forecast: Forecast, temperatureUnit: TemperatureUnit) {
            val unitsString = units2String(temperatureUnit)
            maxTemp?.text = context.getString(R.string.max_temp_format, forecast.getMaxTemp(temperatureUnit), unitsString)
            minTemp?.text = context.getString(R.string.min_temp_format, forecast.getMinTemp(temperatureUnit), unitsString)
        }

    }
}