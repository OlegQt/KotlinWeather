package com.kotlinweather.http


data class CityInfo(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
)
data class CityWeather(
    val name:String,
    val visibility: Int,
    val id:Long,
    val dt:Long,
    val main: CurrentWeather,
    val weather: List<Weather>
) {
    data class CurrentWeather(
        var temp: Double,
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )
}