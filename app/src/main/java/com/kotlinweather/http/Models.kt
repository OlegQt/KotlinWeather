package com.kotlinweather.http

data class City(
    val cityName: String,
    var temperature:Double,
    val lat: Double,
    val lon: Double
)

data class Cities(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
)
data class CityWeather(
    val visibility: Int,
    val main: CurrentWeather
) {
    data class CurrentWeather(
        var temp: Double,
    )
}