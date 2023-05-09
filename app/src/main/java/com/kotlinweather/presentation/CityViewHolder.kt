package com.kotlinweather.presentation

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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
    private val card: MaterialCardView = item.findViewById(R.id.card)

    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtTemperature: TextView = item.findViewById(R.id.txt_city_temperature_main)

    private val btnExpand: Button = item.findViewById(R.id.btn_expand)

    private val layOutCityWeather: LinearLayout = item.findViewById(R.id.layout_city_weather)


    init {
        if(listener.type==1){
            card.isCheckable = false
            layOutCityWeather.visibility = View.GONE
        }
        else
        {
            card.isCheckable = true
            layOutCityWeather.visibility = View.VISIBLE
        }
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

    private fun expand(){

    }

    fun bind(city: CityInfo, weather: CityWeather?) {
        txtCityName.text = city.name

        // Проверяем, если есть информация о погоде
        // Показываем погодный интерфейс
        if (weather != null) {
            txtTemperature.text = weather.main.temp.toString()
                .plus(" \u2103")
        }
        else{
            txtTemperature.text=""
        }

        itemView.setOnClickListener {
            this.card.isChecked = !this.card.isChecked
            listener.onCheckCity(city)
            listener.onCityItemClick(city)
            card.invalidate()
        }
    }

}