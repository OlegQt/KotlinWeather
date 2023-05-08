package com.kotlinweather.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kotlinweather.R
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather
import java.text.SimpleDateFormat
import java.util.*

class CityViewHolder(item: View, private val listener: OnCityClick) :
    RecyclerView.ViewHolder(item) {
    private val layOutWeather: View = item.findViewById(R.id.layout_weather_info)


    private val card: MaterialCardView = item.findViewById(R.id.card)
    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtCountry: TextView = item.findViewById(R.id.txt_secondary_info)
    private val txtLat: TextView = item.findViewById(R.id.txt_support_info)
    private val txtTemperature: TextView = item.findViewById(R.id.txt_city_temperature)
    private val txtWeatherDescription: TextView = item.findViewById(R.id.weather_description)



    init {
        card.isCheckable = listener.type == 0
    }

    private fun CityInfo.print(): String {
        val sLat = String.format("%.3f", this.lat)
        val sLon = String.format("%.3f", this.lon)
        return sLat.plus(" | ").plus(sLon)
    }

    private fun CityWeather.printDate(): String {
        //EEEE dd/MM/yy hh:mm a
        val sdf = SimpleDateFormat("EEEE hh:mm a", Locale.getDefault())
        val netDate = Date(this.dt * 1000L)
        return sdf.format(netDate)
    }

    fun bind(city: CityInfo, weather: CityWeather?) {
        txtCityName.text = city.name
        txtCountry.text = city.country
        txtLat.text = city.print()


        // Проверяем, если есть информация о погоде
        // Показываем погодный интерфейс
        if (weather != null) {
            txtTemperature.text = weather.main.temp.toString()
                .plus(" \u2103")

            txtWeatherDescription.text = weather.weather[0].description
            txtLat.text = weather.printDate()

            layOutWeather.visibility = View.VISIBLE
        } else layOutWeather.visibility = View.GONE



        itemView.setOnClickListener {
            this.card.isChecked = !this.card.isChecked
            listener.onCheckCity(city)
            listener.onCityItemClick(city)
            card.invalidate()
        }


    }

}