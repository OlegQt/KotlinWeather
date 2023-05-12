package com.kotlinweather.http


data class CityInfo(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
)

data class CityWeather(
    val name: String,
    val visibility: Int,
    val id: Long,
    val dt: Long,
    val main: CurrentWeather,
    val wind: Wind,
    val weather: List<Weather>
) {
    data class CurrentWeather(
        val temp: Double,
        var feels_like: Double,
        val pressure: Double,
        val humidity: Double
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    data class Wind(
        val speed: Double
    )
}

data class CityForecast(
    val cnt: Int,
    val list: List<WeatherListItem>
) {
    data class WeatherListItem(
        val dt: Long,
        val main: Main,
        val weather: List<Weather>,
        val visibility: Int,
        val pop: Double,
        val dt_txt: String
    )
    data class Main(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Int,
        val sea_level: Int,
        val grnd_level: Int,
        val humidity: Int,
        val temp_kf: Double
    )
    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

}