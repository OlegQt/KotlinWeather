package com.kotlinweather

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kotlinweather.http.Cities
import com.kotlinweather.http.CityWeather
import com.kotlinweather.http.OpenWeather
import com.kotlinweather.http.Updatable
import com.kotlinweather.presentation.CityAdapter
import com.kotlinweather.presentation.OnCityClick

class MainActivity : AppCompatActivity(), Updatable {
    // Global variables
    private var lblSearchCity: TextInputLayout? = null
    private var txtSearchCity: EditText? = null
    private var rclSearchCity: RecyclerView? = null

    private val weather = OpenWeather(this)
    private val cityData = mutableMapOf<Cities, CityWeather?>()
    //private val searchAdapter = CityAdapter(cityData)

    private lateinit var listener:OnCityClick


    // Functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        deployUi()
        setBehaviour()
    }

    private fun deployUi() {
        lblSearchCity = findViewById(R.id.lbl_search_city)
        txtSearchCity = findViewById(R.id.txt_search_city)

        rclSearchCity = findViewById(R.id.recycler_city_search)
        val layOut = LinearLayoutManager(this)
        layOut.orientation = RecyclerView.VERTICAL
        //rclSearchCity?.adapter = this.searchAdapter
        rclSearchCity?.layoutManager = layOut
    }

    private fun setBehaviour() {
        txtSearchCity?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide keyBoard
                val inputManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                // Search location by city name
                this.weather.getLocations(txtSearchCity?.text.toString())

                //showMessage(txtSearchCity?.text.toString())
            }
            true
        }

        txtSearchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lblSearchCity?.error = ""
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })

        listener = object :OnCityClick{
            override fun onFoundCityClick(city: Cities) {
                showMessage(city.name)
            }

        }

    }

    override fun showFoundCities(cityLocation: List<Cities>) {
        if (cityLocation.isEmpty()) {
            this.lblSearchCity?.error = "City doesn't exists"
        } else {
            val foundCities = mutableMapOf<Cities, CityWeather?>()

            val adapter = CityAdapter(foundCities,listener)
            this.rclSearchCity?.adapter = adapter
            cityLocation.forEach {
                foundCities[it] = null
                adapter.notifyItemInserted(foundCities.size)
            }
        }

    }

    override fun showMessage(msg: String) {
        //TODO("Not yet implemented")
        Snackbar.make(lblSearchCity!!, msg, Snackbar.LENGTH_INDEFINITE)
            .show()
    }

    override fun updateCityCurrentWeather(weather: CityWeather, cityName: String) {
        //TODO("Not yet implemented")
    }
}