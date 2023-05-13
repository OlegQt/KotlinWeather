package com.kotlinweather.http

interface Updatable {
    fun showFoundCities(cityLocation: List<CityInfo>)
    fun showMessage(msg:String)
    fun updateCityCurrentWeather(weather: CityWeather,city: CityInfo)
    fun updateCityForecast(forecast:CityForecast,city:CityInfo)
}