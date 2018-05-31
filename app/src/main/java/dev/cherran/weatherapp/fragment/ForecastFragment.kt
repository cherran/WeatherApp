package dev.cherran.weatherapp.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.view.*
import dev.cherran.weatherapp.model.Forecast
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.activity.SettingsActivity
import dev.cherran.weatherapp.model.City
import dev.cherran.weatherapp.model.TemperatureUnit
import kotlinx.android.synthetic.main.fragment_forecast.*

class ForecastFragment: Fragment() {


    companion object {
        val ARG_CITY = "ARG_CITY"

        // Patrón muy común en Android para la creación de Fragments
        fun newInstance(city: City): Fragment {
            // Creamos el fragment
            val fragment = ForecastFragment()

            // Creamos los argumentos del fragment
            val arguments = Bundle()
            arguments.putSerializable(ARG_CITY, city)

            // Asignamos los argumentos al fragment
            fragment.arguments = arguments

            // Devolvemos el fragment
            return fragment
        }

    }

    val REQUEST_SETTINGS = 1
    val PREFERENCE_UNITS = "PREFERENCE_UNITS"


    var forecast: Forecast? = null
        set(value) {
            field = value /////////////////// Así guardo value en forecast
            if (value != null) {
                forecast_image.setImageResource(value.icon)
                forecast_description.text = value.description

                updateTemperatureView()
                humidity.text = getString(R.string.humidity_format, value.humidity)
            }
        }


    val units: TemperatureUnit
        get() = when(PreferenceManager.getDefaultSharedPreferences(activity)
                .getInt(PREFERENCE_UNITS, TemperatureUnit.CELSIUS.ordinal)) {
            TemperatureUnit.CELSIUS.ordinal -> TemperatureUnit.CELSIUS
            else -> TemperatureUnit.FAHRENHEIT
        }


    // No se actualiza la interfaz aquí, de momento solo lo utilizamos para el setHasOptionsMenu(true). Puede ser opcional
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    // Muy importante, aquí se infla el XML de la vista
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater?.inflate(R.layout.fragment_forecast, container, false)
        //  attachToRoot: false --> ya se encarga de hacerlo la Activity
        return root!!
    }


    // Aquí la interfaz ya ha sido creada, podemos empezar a modificarla
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // En el caso de los Fragments no se puede actualizar la vista hasta que esta ha sido creada

        if(arguments != null) {
            val city = arguments.getSerializable(ARG_CITY) as City
            forecast = city.forecast
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_forecast, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_show_settings -> {
                // Lanzaremos la pantalla de ajustes

//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)

                // Con el patrón de los Intents, lo hacemos más sencillito
                startActivityForResult(SettingsActivity.intent(activity, units),
                        REQUEST_SETTINGS)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Volvemos de settings con datos sobre las unidades elegidas por el user
                    val newUnits = data.getSerializableExtra(SettingsActivity.EXTRA_UNITS) as TemperatureUnit
                    val oldUnits = units // Las almaceno por si el usuario deshace el cambio de unidades en el Snackbar

                    // Guardo las preferencias del usuario
                    PreferenceManager.getDefaultSharedPreferences(activity)
                            .edit()
                            .putInt(PREFERENCE_UNITS, newUnits.ordinal)
                            .apply() // Para hacerlo de forma asíncrona

                    // Actualizo la interfaz con las nuevas unidades
                    updateTemperatureView()

                    val newUnitsString = if (newUnits == TemperatureUnit.CELSIUS) getString(R.string.user_selects_celsius)
                    else getString(R.string.user_selects_fahrenheit)

                    // Toast.makeText(this, newUnitsString, Toast.LENGTH_LONG).show()
                    Snackbar.make(view, newUnitsString, Snackbar.LENGTH_LONG)
//                            .setAction(getString(R.string.undo), View.OnClickListener {
//                                // Guardo las unidades viejas
//                                PreferenceManager.getDefaultSharedPreferences(this)
//                                        .edit()
//                                        .putInt(PREFERENCE_UNITS, oldUnits.ordinal)
//                                        .apply() // Para hacerlo de forma asíncrona
//
//                                // Actualizo la interfaz con las nuevas unidades
//                                updateTemperatureView()
//                            })
                            .setAction(getString(R.string.undo)) {// El último argumento de setAction es un closure (bloque dde código), por lo que se puede pasar así (trailing closure) para simplificarlo
                                // Guardo las unidades viejas
                                PreferenceManager.getDefaultSharedPreferences(activity)
                                        .edit()
                                        .putInt(PREFERENCE_UNITS, oldUnits.ordinal)
                                        .apply() // Para hacerlo de forma asíncrona

                                // Actualizo la interfaz con las nuevas unidades
                                updateTemperatureView()
                            }
                            .show()
                }
            }
        }
    }

    // Aquí actualizamos la interfaz con las temperaturas
    private fun updateTemperatureView() {
        max_temp.text = getString(R.string.max_temp_format, forecast?.getMaxTemp(units), unitsToString()) // max_temp_format tiene parámetros, así que le paso también la temperatura
        min_temp.text = getString(R.string.min_temp_format, forecast?.getMinTemp(units), unitsToString())
    }

    private fun unitsToString() = if (units == TemperatureUnit.CELSIUS) "ºC"
    else "F"

}