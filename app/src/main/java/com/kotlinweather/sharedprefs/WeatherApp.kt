package com.kotlinweather.sharedprefs

import android.app.Application
import android.content.SharedPreferences

class WeatherApp:Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        //val str = sharedPreferences.getString(APP_PREFERENCES_CITIES,"something")
    }

    companion object{
        const val APP_PREFERENCES = "key_for_weather_application"
        const val APP_PREFERENCES_CITIES = "city_data_storage"


        lateinit var instance:WeatherApp
        lateinit var sharedPreferences:SharedPreferences
    }
}