package com.kotlinweather.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("/geo/1.0/direct")
    fun getCitiesLocation(
        @Query("q") cityName: String,
        @Query("appid") appKey: String,
        @Query("limit") num: Int,
        @Query("lang") language: String
    ): Call<List<CityInfo>>

    @GET("/data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appKey: String,
        @Query("units") options: String
    ): Call<CityWeather>

    @GET("/data/2.5/weather")
    fun requestWeatherByCityName(
        @Query("q") cityName: String,
        @Query("units") options: String,
        @Query("appid") appKey: String,
        @Query("lang") language: String
    ): Call<CityWeather>
}