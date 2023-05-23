package com.kotlinweather.http

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class OpenWeather() {
    private val baseUrl = "https://api.openweathermap.org/"
    private val appKey = "ce2b06f255f2307c07504a706c9d920d"
    private val TAG = "OpenWeather"


    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openWeather: OpenWeatherApi = retrofit.create(OpenWeatherApi::class.java)

    fun getCity(cityName: String):Call<kotlin.collections.List<CityInfo>>{
        return openWeather.getCitiesLocation(cityName, appKey, 10, "ru")
    }





    fun getLocations(cityName: String) {
        val call = openWeather.getCitiesLocation(cityName, appKey, 10, "ru")
        call.enqueue(object : Callback<List<CityInfo>> {
            override fun onResponse(
                call: Call<List<CityInfo>>,
                response: Response<List<CityInfo>>
            ) {
                Log.d(TAG, response.code().toString())
                if (response.code() == 200) {
                    //if (response.body() != null) listener?.showFoundCities(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<CityInfo>>, t: Throwable) {
                Log.d(TAG, "fail")
            }

        })
    }

    fun getWeather(city: CityInfo) {
        val call = openWeather.getWeather(city.lat, city.lon, appKey, "metric")
        call.enqueue(object : Callback<CityWeather> {
            override fun onResponse(call: Call<CityWeather>, response: Response<CityWeather>) {
                if (response.body() != null) {
                    //listener?.updateCityCurrentWeather(response.body()!!, city)
                }
            }

            override fun onFailure(call: Call<CityWeather>, t: Throwable) {
                Log.d(TAG, "Weather fail")
            }
        })
    }

    fun requestWeather(cityInfo: CityInfo) {
        val call = openWeather.requestWeatherByCityName(cityInfo.name, "metric", appKey, "ru")
        call.enqueue(object : Callback<CityWeather> {
            override fun onResponse(call: Call<CityWeather>, response: Response<CityWeather>) {
                Log.d(TAG, "${response.code()}")
                val pW: CityWeather? = response.body()
                var temp = pW?.main?.temp
                Log.d(TAG, "${response.code()}")
                if (pW != null) {
                    val t = pW.weather
                }
            }

            override fun onFailure(call: Call<CityWeather>, t: Throwable) {
                //
            }

        })
    }

    fun requestForecast(city: CityInfo) {
        val call = openWeather.requestWeatherForecast(city.lat, city.lon, appKey, "metric")
        call.enqueue(object : Callback<CityForecast> {
            override fun onResponse(call: Call<CityForecast>, response: Response<CityForecast>) {
                Log.d(TAG, response.code().toString())
                if (response.code() == 200) {
                    val lst = response?.body()
                    val str = StringBuilder()

                    val sdf = SimpleDateFormat("EEEE hh:mm a", Locale.getDefault())
                    //val netDate = Date( * 1000L)
                    //return sdf.format(netDate)

                    lst?.list?.forEach {
                        val netDate = Date(it.dt * 1000L)
                        str.append(sdf.format(netDate).toString()).append(" - ")
                        str.append(it.main.feels_like.toString()).append("\n")
                    }
                    //val mn = lst?.city?.name
                    //listener?.updateCityForecast(lst!!,city)
                }
            }

            override fun onFailure(call: Call<CityForecast>, t: Throwable) {
                //
            }

        })
    }
}