package dev.cherran.weatherapp.activity


import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.fragment.ForecastFragment
import dev.cherran.weatherapp.model.Cities
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

        val adapter = object: FragmentPagerAdapter(supportFragmentManager) { /////////// Clase anónima
            override fun getItem(position: Int): Fragment {
                return ForecastFragment.newInstance(Cities.getCity(position))
            }

            override fun getCount(): Int = Cities.count

            override fun getPageTitle(position: Int): CharSequence? {
                return Cities.getCity(position).name
            }

        }

        view_pager.adapter = adapter
        view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                updateCityInfo(position)
            }
        })

        val initialCityIndex = intent.getIntExtra(EXTRA_CITY, 0)
        moveToCity(intent.getIntExtra(EXTRA_CITY, 0))
        updateCityInfo(initialCityIndex)
    }


    private fun updateCityInfo(position: Int) {
        supportActionBar?.title = Cities.getCity(position).name
    }


    private fun moveToCity(position: Int) {
        view_pager.currentItem = position
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.pager_navigation, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        R.id.previous -> {
            view_pager.currentItem = view_pager.currentItem - 1
            true
        }
        R.id.next -> {
            view_pager.currentItem = view_pager.currentItem + 1
            true
        }
        android.R.id.home -> { // flecha de atrás de la toolbar
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // Se llama cada vez que cambiamos de página
        super.onPrepareOptionsMenu(menu)

        val previousMenu: MenuItem? = menu?.findItem(R.id.previous)
        val nextMenu: MenuItem? = menu?.findItem(R.id.next)

        val adapter: PagerAdapter = view_pager.adapter!!
        previousMenu?.isEnabled = view_pager.currentItem > 0
        nextMenu?.isEnabled = view_pager.currentItem < adapter.count - 1

        return true
    }
}
