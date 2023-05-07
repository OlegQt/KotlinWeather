package com.kotlinweather.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kotlinweather.R
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather

class CityViewHolder(item: View, private val listener: OnCityClick) :
    RecyclerView.ViewHolder(item) {
    private val layOutWeather: View = item.findViewById(R.id.layout_weather_info)


    private val card: MaterialCardView = item.findViewById(R.id.card)
    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtCountry: TextView = item.findViewById(R.id.txt_secondary_info)
    private val txtLat: TextView = item.findViewById(R.id.txt_support_info)
    private val txtTemperature: TextView = item.findViewById(R.id.txt_city_temperature)


    init {
        if (listener.type == 0) {
            //val colorSalmon = ContextCompat.getColor(item.context,R.color.salmon)
            //card.setCardBackgroundColor(colorSalmon)
            //card.isCheckable = true
            //card.checkedIconGravity = MaterialCardView.CHECKED_ICON_GRAVITY_BOTTOM_END
            //txtCityName.setBackgroundColor(colorSalmon)
        }
    }

    private fun CityInfo.print(): String {
        val sLat = String.format("%.3f", this.lat)
        val sLon = String.format("%.3f", this.lon)
        return sLat.plus(" | ").plus(sLon)
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
            layOutWeather.visibility = View.VISIBLE
        } else layOutWeather.visibility = View.GONE



        itemView.setOnClickListener {
            this.listener.onCityItemClick(city)
            this.card.isChecked = !this.card.isChecked
            if(card.isChecked) listener.onCheckCity(city)
            card.invalidate()
        }


    }

}