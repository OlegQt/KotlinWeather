package com.kotlinweather.http

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeather(var listener: Updatable?) {
    private val baseUrl = "https://api.openweathermap.org/"
    private val appKey = "ce2b06f255f2307c07504a706c9d920d"
    private val TAG = "OpenWeather"


    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openWeather: OpenWeatherApi = retrofit.create(OpenWeatherApi::class.java)

    fun setNewListener(listener: Updatable) {
        this.listener = listener
    }

    fun getLocations(cityName: String) {
        val call = openWeather.getCitiesLocation(cityName, appKey,10)
        call.enqueue(object : Callback<List<Cities>> {
            override fun onResponse(
                call: Call<List<Cities>>,
                response: Response<List<Cities>>
            ) {
                Log.d(TAG,response.code().toString())
                if (response.code() == 200) {
                    val citiesLocations = mutableListOf<Cities>()
                    if (!response.body().isNullOrEmpty()) {
                        response.body()?.forEach {
                            Log.d(TAG,"${it.name} ${it.country} ${it.lon}   ${it.lon}")
                            citiesLocations.add(it)
                        }

                        //listener?.insertCity(citiesLocations)
                    }
                }
            }

            override fun onFailure(call: Call<List<Cities>>, t: Throwable) {
                Log.d(TAG, "fail")
            }

        })
    }

    fun getWeather(lat: Double, lon: Double,cityName: String) {
        val call = openWeather.getWeather(lat, lon, appKey, "metric")
        call.enqueue(object : Callback<CityWeather> {
            override fun onResponse(call: Call<CityWeather>, response: Response<CityWeather>) {
                if (response.body() != null) {
                    listener?.updateCityCurrentWeather(response.body()!!,cityName)
                }
            }

            override fun onFailure(call: Call<CityWeather>, t: Throwable) {
                Log.d(TAG, "Weather fail")
            }
        })
    }

}