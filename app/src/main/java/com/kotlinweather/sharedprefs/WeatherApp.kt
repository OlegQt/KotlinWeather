package com.kotlinweather.sharedprefs

import android.app.Application
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class WeatherApp:Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        //val str = sharedPreferences.getString(APP_PREFERENCES_CITIES,"something")
    }

    fun getDateFromLong(dateFormat:String,date:Long):String{
        //EEEE dd/MM/yy hh:mm a
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val netDate = Date(date * 1000L)
        return sdf.format(netDate)
    }

    companion object{
        const val APP_PREFERENCES = "key_for_weather_application"
        const val APP_PREFERENCES_CITIES = "city_data_storage"


        lateinit var instance:WeatherApp
        lateinit var sharedPreferences:SharedPreferences
    }
}