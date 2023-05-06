package com.kotlinweather.http

interface Updatable {
    fun showFoundCities(cityLocation: List<Cities>)
    fun showMessage(msg:String)
    fun updateCityCurrentWeather(weather: CityWeather,cityName: String)
}