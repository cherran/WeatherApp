package dev.cherran.weatherapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.model.TemperatureUnit
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    // Patrón muy utilizado en Android para los Intents
    companion object {
        val EXTRA_UNITS = "EXTRA_UNITS"

        fun intent(context: Context, initialUnits: TemperatureUnit): Intent {
            val settingsIntent = Intent(context, SettingsActivity::class.java)

            settingsIntent.putExtra(EXTRA_UNITS, initialUnits)

            return settingsIntent
        }
    }

    val initialUnits by lazy {
        intent.getSerializableExtra(EXTRA_UNITS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ok_btn.setOnClickListener { acceptSettings() }
        cancel_btn.setOnClickListener { cancelSettings() }

        // Decidimos qué radioButtton tiene que estar marcado en función de initialUnits
        if (initialUnits == TemperatureUnit.CELSIUS) {
            units_rg.check(R.id.celsius_rb)
        } else {
            units_rg.check(R.id.farenheit_rb)
        }

        // Esta forma de usar if es muy común en Kotlin, pero puede sonar a chino:
//        units_rg.check(if (initialUnits == TemperatureUnit.CELSIUS) R.id.celsius_rb
//        else R.id.farenheit_rb)

    }

    private fun cancelSettings() {
        // Volver a la activity anterior (hago pop de esta Activity en el stack de vistas)
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun acceptSettings() {
        // Creamos los datos de regreso, en este caso las unidades elegidas
        val returnIntent = Intent()
        when (units_rg.checkedRadioButtonId) {
            R.id.celsius_rb -> returnIntent.putExtra(EXTRA_UNITS, TemperatureUnit.CELSIUS)
            R.id.farenheit_rb -> returnIntent.putExtra(EXTRA_UNITS, TemperatureUnit.FAHRENHEIT)
        }
        setResult(Activity.RESULT_OK, returnIntent)

        // Finalizamos la actividad para volver a la anterior
        finish()
    }
}
