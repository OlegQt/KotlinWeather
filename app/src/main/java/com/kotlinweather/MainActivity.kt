package com.kotlinweather

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather
import com.kotlinweather.http.OpenWeather
import com.kotlinweather.http.Updatable
import com.kotlinweather.presentation.CityAdapter
import com.kotlinweather.presentation.OnCityClick
import com.kotlinweather.sharedprefs.WeatherApp

class MainActivity : AppCompatActivity(), Updatable {
    // Global variables
    private var lblSearchCity: TextInputLayout? = null
    private var txtSearchCity: EditText? = null
    private var rclSearchCity: RecyclerView? = null
    private var layOutAction: LinearLayout? = null
    private var btnWeatherUpdate: Button? = null
    private var btnTest: Button? = null
    private var btnDelete: Button? = null


    private val weather = OpenWeather(this)
    private val cityData = mutableMapOf<CityInfo, CityWeather?>()
    private val setCityToDelete = mutableSetOf<CityInfo>()

    private lateinit var cityAdapter: CityAdapter


    // Functions
    private fun insertCity(city: CityInfo) {
        if (this.cityData.contains(city)) showMessage("${city.name} already exists!")
        else {
            cityData[city] = null
            saveCitiesToPrefs()
        }
    }

    private fun saveCitiesToPrefs() {
        val citiesList = cityData.keys.toList()
        val jsonCitiesList = Gson().toJson(citiesList)
        WeatherApp.sharedPreferences.edit()
            .putString(WeatherApp.APP_PREFERENCES_CITIES, jsonCitiesList).apply()
    }

    private fun deployUi() {
        lblSearchCity = findViewById(R.id.lbl_search_city)
        txtSearchCity = findViewById(R.id.txt_search_city)
        layOutAction = findViewById(R.id.lout_action)
        rclSearchCity = findViewById(R.id.recycler_city_search)
        btnWeatherUpdate = findViewById(R.id.btn_update_weather)
        btnTest = findViewById(R.id.btn_test)
        btnDelete = findViewById(R.id.btn_delete)

        val layOut = LinearLayoutManager(this)
        layOut.orientation = RecyclerView.VERTICAL
        rclSearchCity?.layoutManager = layOut

        showCityRecycler(true)
    }

    private fun setBehaviour() {

        txtSearchCity?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Hide keyBoard
                val inputManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                // Hide main CityRecycler
                this.showCityRecycler(false)

                // Search location by city name
                this.weather.getLocations(txtSearchCity?.text.toString())
            }
            true
        }

        txtSearchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lblSearchCity?.error = ""
                if (s.isNullOrEmpty()) {
                    //rclSearchCity?.adapter = cityAdapter
                    showCityRecycler(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })

        btnWeatherUpdate?.setOnClickListener {
            if (cityData.isNotEmpty()) {
                cityData.forEach {
                    weather.getWeather(it.key)
                }
            }
        }

        btnDelete?.setOnClickListener {
            if (setCityToDelete.isEmpty()) {
                showMessage("Nothing selected")
            } else {
                val iterator = setCityToDelete.iterator()
                while (iterator.hasNext()) {
                    val city = iterator.next()
                    val position = cityData.keys.indexOf(city)
                    cityData.remove(city)
                    rclSearchCity?.adapter?.notifyItemRemoved(position)
                    iterator.remove()
                }
                saveCitiesToPrefs()
            }
        }

        btnTest?.setOnClickListener {
            weather.requestWeather(CityInfo("moscow",0.0,0.0,"RU"))
        }
    }

    private fun showCityRecycler(visibility: Boolean) {

        if (visibility and cityData.isNotEmpty()) {
            rclSearchCity?.adapter = cityAdapter
            layOutAction?.visibility = View.VISIBLE
        } else layOutAction?.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        deployUi()
        setBehaviour()

        val str =
            WeatherApp.sharedPreferences.getString(WeatherApp.APP_PREFERENCES_CITIES, "something")
        if (!str.equals("something")) {
            val data = Gson().fromJson(str, Array<CityInfo>::class.java)
            if (data.isNotEmpty()) {
                cityData.clear()
                data.forEach {
                    cityData.put(it, null)
                }
            }
        }

        // Инициализируем основной адаптер
        // В идеале надо загрузить данные из SharedPrefs
        val listener2 = object : OnCityClick {
            override val type: Int = 0

            override fun onCheckCity(city: CityInfo) {
                val isInSet = setCityToDelete.add(city)
                if (!isInSet) setCityToDelete.remove(city)
            }
        }
        this.cityAdapter = CityAdapter(cityData, listener2)
        showCityRecycler(true)
    }

    override fun showFoundCities(cityLocation: List<CityInfo>) {
        if (cityLocation.isEmpty()) {
            this.lblSearchCity?.error = "City doesn't exists"
        } else {
            val foundCityInfo = mutableMapOf<CityInfo, CityWeather?>()
            val listener = object : OnCityClick {
                override val type: Int = 1
                override fun onCityItemClick(city: CityInfo) {
                    //showMessage(city.name)
                    insertCity(city)
                }
            }

            val adapter = CityAdapter(foundCityInfo, listener)
            this.rclSearchCity?.adapter = adapter
            cityLocation.forEach {
                foundCityInfo[it] = null
                adapter.notifyItemInserted(foundCityInfo.size)
            }
        }

    }

    override fun showMessage(msg: String) {
        //TODO("Not yet implemented")
        Snackbar.make(lblSearchCity!!, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("Ok", {})
            .show()
    }

    override fun updateCityCurrentWeather(weather: CityWeather, city: CityInfo) {
        cityData[city] = weather
        val i = cityData.keys.indexOf(city)
        cityAdapter.notifyItemChanged(i)

    }
}