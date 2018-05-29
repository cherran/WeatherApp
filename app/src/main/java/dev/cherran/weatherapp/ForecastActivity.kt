package dev.cherran.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_forecast.*


class ForecastActivity : AppCompatActivity() {
// En Kotlin al heredar de una clase se pone el constructor de la superclase por defecto (En este caso sin argumento)

    val TAG = ForecastActivity::class.java.canonicalName // Buena práctica para establecer el TAG de los logs de cada clase
    // var forecastImage: ImageView? = null
    // lateinit var forecastImage: ImageView
//    val forecastImage by lazy {
//        // La primera vez que se acceda en el código al valor de esta variable se calcula el mismo (la última línea de lo que hay dentro de este closure)
//        findViewById<ImageView>(R.id.forecast_image)
//    }

    override fun onCreate(savedInstanceState: Bundle?) { // Bundle? -> Optional
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast) // Hasta aquí no puedo acceder a los elementos de la vista
        // var number = savedInstanceState?.getInt("Number")

        Log.v(TAG, "Han llamado a OnCreate")
/*

//        val europeanButton = findViewById(R.id.european_system_button) as? Button
        // Todas estas opciones son igualmente válidas
        // val europeanButton = findViewById<Button>(R.id.european_system_button)
        // val europeanButton: Button = findViewById(R.id.european_system_button)

//        val americanButton = findViewById(R.id.american_system_button) as? Button

        // forecastImage = findViewById(R.id.forecast_image)

//        europeanButton?.setOnClickListener(this)
//        americanButton?.setOnClickListener(this)

        // Equivalente al if-let de Swift
//        if (europeanButton != null) {
//            europeanButton.setOnClickListener(this)
//        }

        // Con closures
//        european_system_button.setOnClickListener {
//            forecast_image.setImageResource(R.drawable.offline_weather)
//        }
//
//        american_system_button.setOnClickListener {
//            forecast_image.setImageResource(R.drawable.offline_weather2)
//        }

*/


        val forecast = Forecast(25f,
                                10f,
                                35f,
                                "Soleado con alguna nube",
                                 R.drawable.ico_01) // 25f ->  Float(25)
        // forecast.minTemp = 12f // Puedo hacer esto si en el constructor de Forecast está definida como var

        setForecast(forecast)
    }




    fun setForecast(forecast: Forecast) {
        forecast_image.setImageResource(forecast.icon)
        forecast_description.text = forecast.description

        max_temp.text = getString(R.string.max_temp_format, forecast.maxTemp) // max_temp_format tiene parámetros, así que le paso también la temperatura
        min_temp.text = getString(R.string.min_temp_format, forecast.minTemp)
        humidity.text = getString(R.string.humidity_format, forecast.humidity)
    }

/*
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        // Para guardar datos antes de la destrucción de la vista en el outState (Diccionario)
        outState?.putInt("Number", 3)
    }
*/


/*    override fun onClick(v: View?) {
        // v.getId() es lo mismo que v.id
        *//* Esta es una forma muy normal de usar un if
        if (v?.id == R.id.european_system_button) {
            Log.v(TAG, "Han pulsado el botón europeo")
        }
        else if (v?.id == R.id.american_system_button) {
            Log.v(TAG, "Han pulsado el botón americano")
        }
        else {
            Log.w(TAG, "No tengo ni idea de qué es lo que han pulsado")
        }*//*

        // En Kotlin no hay Switch statement, hay When
//      when (v?.id) {
//          R.id.european_system_button -> Log.v(TAG, "Han pulsado el botón europeo")
//          R.id.american_system_button -> Log.v(TAG, "Han pulsado el botón americano")
//          else -> Log.w(TAG, "No tengo ni idea de qué es lo que han pulsado")
//       }

       // Más PRO
//       Log.v(TAG, when (v?.id) {
//           R.id.european_system_button -> "Han pulsado el botón europeo"
//           R.id.american_system_button -> "Han pulsado el botón americano"
//           else -> "No tengo ni idea de qué es lo que han pulsado"
//       })

        val imageToShow = when (v?.id) {
            R.id.european_system_button -> R.drawable.offline_weather
            else -> R.drawable.offline_weather2
        }

        forecastImage?.setImageResource(imageToShow) // Solo se llama al método si forecastImage != null

    }*/
}
