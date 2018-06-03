package dev.cherran.weatherapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.model.TemperatureUnit
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsDialog: DialogFragment() {

    companion object {

        val ARG_UNITS = "ARG_UNITS"

        fun newInstance(initialUnits: TemperatureUnit): SettingsDialog {
            val arguments = Bundle()
            arguments.putSerializable(ARG_UNITS, initialUnits)

            val dialog = SettingsDialog()
            dialog.arguments = arguments

            return dialog
        }
    }

    private val initialUnits by lazy {
        arguments?.getSerializable(ARG_UNITS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ok_btn.setOnClickListener { acceptSettings() }
        cancel_btn.setOnClickListener { cancelSettings() }

        if (initialUnits == TemperatureUnit.CELSIUS) {
            units_rg.check(R.id.celsius_rb)
        }
        else {
            units_rg.check(R.id.farenheit_rb)
        }

        // Hacemos una animación al arrancar
        units_rg.visibility = View.GONE
        units_rg.postDelayed({
            units_rg.visibility = View.VISIBLE
        }, resources.getInteger(R.integer.default_fake_loading_time).toLong())
    }

    private fun cancelSettings() {
        // Inidicamos que cancelamos el envío de datos
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
        dismiss()
    }

    private fun acceptSettings() {
        // Creamos los datos de regreso, en este caso las unidades elegidas
        val returnIntent = Intent()
        when (units_rg.checkedRadioButtonId) {
            R.id.celsius_rb -> returnIntent.putExtra(ARG_UNITS, TemperatureUnit.CELSIUS)
            R.id.farenheit_rb -> returnIntent.putExtra(ARG_UNITS, TemperatureUnit.FAHRENHEIT)
        }

        // Lo mismo que en cancel, pero devolviendo datos
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, returnIntent)

        dismiss()
    }

}