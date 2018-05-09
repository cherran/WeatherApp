package dev.cherran.weatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

class MainActivity : AppCompatActivity() {
// En Kotlin al heredar de una clase se pone el constructor de la superclase por defecto (En este caso sin argumento)

    override fun onCreate(savedInstanceState: Bundle?) { // Bundle? -> Optional
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var number = savedInstanceState?.getInt("Number")

        Log.v("aTag", "Han llamado a OnCreate V")
        Log.d("aTag", "Han llamado a OnCreate")
        Log.e("aTag", "Han llamado a onCreate")
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        // Para guardar datos antes de la destrucción de la vista en el outState (Diccionario)
        outState?.putInt("Number", 3)
    }
}
