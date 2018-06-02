package dev.cherran.weatherapp.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.fragment.CityPagerFragment
import kotlinx.android.synthetic.main.activity_city_pager.*

class CityPagerActivity : AppCompatActivity() {

    companion object {

        val EXTRA_CITY = "EXTRA_CITY"

        fun intent(context: Context, cityIndex: Int): Intent {
            val intent = Intent(context, CityPagerActivity::class.java)
            intent.putExtra(EXTRA_CITY, cityIndex)

            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_city_pager)

        // toolbar.setLogo(R.mipmap.ic_launcher)
        setSupportActionBar(toolbar) // Para hacer que la toolbar haga de ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // botón de "atrás" en la toolbar

        val initialCityIndex = intent.getIntExtra(EXTRA_CITY, 0)

        // Creo, si hace falta, el CityPagerFragment, pasándole la ciudad inicial
        if (supportFragmentManager.findFragmentById(R.id.view_pager_fragment) == null) {
            // Hay hueco, y habiéndolo, metemos el fragment
            val fragment = CityPagerFragment.newInstance(initialCityIndex)
            supportFragmentManager.beginTransaction()
                    .add(R.id.view_pager_fragment, fragment)
                    .commit()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.pager_navigation, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> { // flecha de atrás de la toolbar
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
