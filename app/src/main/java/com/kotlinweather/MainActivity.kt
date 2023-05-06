package com.kotlinweather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kotlinweather.http.Cities
import com.kotlinweather.http.CityWeather
import com.kotlinweather.http.Updatable

class MainActivity : AppCompatActivity(),Updatable {
    // Global variables
    private var lblSearchCity:TextInputLayout? = null
    private var txtSearchCity:EditText? = null

    // Functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deployUi()
        setBehaviour()


    }

    private fun deployUi(){
        lblSearchCity = findViewById(R.id.lbl_search_city)
        txtSearchCity = findViewById(R.id.txt_search_city)




    }


    private fun setBehaviour(){
        txtSearchCity?.setOnEditorActionListener { v, actionId, event ->
            if(actionId==EditorInfo.IME_ACTION_DONE){
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                showMessage(txtSearchCity?.text.toString())
            }
            true
        }

        txtSearchCity?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               //lblSearchCity?.error = "city doesn't found"
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })

    }

    override fun insertCity(cityLocation: List<Cities>) {
        //TODO("Not yet implemented")
    }

    override fun showMessage(msg: String) {
        //TODO("Not yet implemented")
        Snackbar.make(lblSearchCity!!,msg,Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(lblSearchCity)
            .show()
    }

    override fun updateCityCurrentWeather(weather: CityWeather, cityName: String) {
        //TODO("Not yet implemented")
    }
}