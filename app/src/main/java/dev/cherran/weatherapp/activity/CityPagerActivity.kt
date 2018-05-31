package dev.cherran.weatherapp.activity

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter
import dev.cherran.weatherapp.R
import dev.cherran.weatherapp.fragment.ForecastFragment
import dev.cherran.weatherapp.model.Cities
import kotlinx.android.synthetic.main.activity_city_pager.*

class CityPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_pager)

        val cities = Cities()

        val adapter = object: FragmentPagerAdapter(fragmentManager) { /////////// Clase anónima
            override fun getItem(position: Int): Fragment {
                return ForecastFragment.newInstance(cities.getCity(position))
            }

            override fun getCount(): Int = cities.count

            override fun getPageTitle(position: Int): CharSequence? {
                return cities.getCity(position).name
            }

        }

        view_pager.adapter = adapter
    }
}
