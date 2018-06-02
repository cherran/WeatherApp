package dev.cherran.weatherapp.fragment

import android.app.Activity
import android.support.v4.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dev.cherran.weatherapp.model.Forecast
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.activity.SettingsActivity
import dev.cherran.weatherapp.adapter.ForecastRecyclerViewAdapter
import dev.cherran.weatherapp.getTemperatureUnits
import dev.cherran.weatherapp.model.City
import dev.cherran.weatherapp.model.TemperatureUnit
import dev.cherran.weatherapp.setTemperatureUnits
import kotlinx.android.synthetic.main.content_forecast.*
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

    private enum class VIEW_INDEX(val index: Int) {
        LOADING(0), FORECAST(1)
    }


    val REQUEST_SETTINGS = 1
    val PREFERENCE_UNITS = "PREFERENCE_UNITS"


    var forecast: List<Forecast>? = null
        set(value) {
            field = value /////////////////// Así guardo value en forecast
    if (value != null) {
                val adapter = ForecastRecyclerViewAdapter(value)
                forecast_list?.adapter = adapter
            }
        }




    // No se actualiza la interfaz aquí, de momento solo lo utilizamos para el setHasOptionsMenu(true). Puede ser opcional
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }



    // Aquí la interfaz ya ha sido creada, podemos empezar a modificarla
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // En el caso de los Fragments no se puede actualizar la vista hasta que esta ha sido creada

        // Configuramos las animaciones para el ViewSwitcher
        view_switcher.setInAnimation(activity, android.R.anim.fade_in)
        view_switcher.setOutAnimation(activity, android.R.anim.fade_out)

        // Le decimos al ViewSwitcher que muestre la primera vista
        view_switcher.displayedChild = VIEW_INDEX.LOADING.index

        view.postDelayed({
            // Aquí simulamos que ya nos hemos bajado la información del tiempo

            // Configuramos el RecyclerView. Primero decimos cómo se visualizan sus elementos
            forecast_list?.layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.forecast_columns))

            // Le decimos quién es el que anima al RecyclerView
            forecast_list?.itemAnimator = DefaultItemAnimator()

            // Por último tenemos que decirle los datos que van a rellenar el RecyclerView.
            // No se hace aquí, eso es tarea del setter de forecast

            val city = arguments?.getSerializable(ARG_CITY) as City
            forecast = city.forecast
            view_switcher?.displayedChild = VIEW_INDEX.FORECAST.index
        }, resources.getInteger(R.integer.default_fake_loading_time).toLong())

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
                startActivityForResult(SettingsActivity.intent(activity!!, getTemperatureUnits(activity!!)),
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
                    val oldUnits = getTemperatureUnits(activity!!) // Las almaceno por si el usuario deshace el cambio de unidades en el Snackbar

                    // Guardo las preferencias del usuario
                    setTemperatureUnits(activity!!, newUnits)

                    // Actualizo la interfaz con las nuevas unidades
                    updateTemperatureView()

                    val newUnitsString = if (newUnits == TemperatureUnit.CELSIUS) getString(R.string.user_selects_celsius)
                    else getString(R.string.user_selects_fahrenheit)

                    // Toast.makeText(this, newUnitsString, Toast.LENGTH_LONG).show()
                    Snackbar.make(view!!, newUnitsString, Snackbar.LENGTH_LONG)
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
                            .setAction(getString(R.string.undo)) {// El último argumento de setAction es un closure (bloque de código), por lo que se puede pasar así (trailing closure) para simplificarlo
                                // Guardo las unidades viejas
                                setTemperatureUnits(activity!!, oldUnits)

                                // Actualizo la interfaz con las nuevas unidades
                                updateTemperatureView()
                            }
                            .show()
                }
            }
        }
    }

    // Se llama cada vez que cambia la visibilidad de este frasgment al user (si lo ve o no)
    // Fixes a problem/bug when we change units and we move to the next city
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if(isVisibleToUser && forecast != null) updateTemperatureView()
    }



    // Aquí actualizamos la interfaz con las temperaturas
    private fun updateTemperatureView() {
        forecast_list?.adapter = ForecastRecyclerViewAdapter(forecast!!)
    }


}