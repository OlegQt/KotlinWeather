package com.kotlinweather.presentation

import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.kotlinweather.R
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.exp

class CityViewHolder(item: View, private val listener: OnCityClick) :
    RecyclerView.ViewHolder(item) {
    private val card: MaterialCardView = item.findViewById(R.id.card)

    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtTemperature: TextView = item.findViewById(R.id.txt_city_temperature_main)
    private val txtTempDescription: TextView = item.findViewById(R.id.txt_weather_description)


    private val btnExpand: Button = item.findViewById(R.id.btn_expand)
    private val imgWeather: ImageView = item.findViewById(R.id.img_weather)


    private val layOutCityExpand: RelativeLayout = item.findViewById(R.id.expandable_layout)
    private val layOutHiddenInfo: RelativeLayout = item.findViewById(R.id.hidden_weather_info)

    private var isExpanded = false



    init {
        if(listener.type==1){
            card.isCheckable = false
            layOutHiddenInfo.visibility = View.GONE
            layOutCityExpand.visibility = View.GONE
        }
        else
        {
            card.isCheckable = true
            layOutCityExpand.visibility = View.VISIBLE
            if (isExpanded) layOutHiddenInfo.visibility = View.VISIBLE
            else layOutHiddenInfo.visibility = View.GONE
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

    private fun CityWeather.printTemperatureLine():String{
        val str:String = String()
            .plus("Feels like ${this.main.feels_like}")
            .plus(" \u2103.")
            .plus(" ${this.weather.first().description}. ")
            .plus("Wind ${this.wind.speed}")
        return str
    }

    private fun expand(){
        if (isExpanded){
            this.layOutHiddenInfo.visibility = View.GONE
            btnExpand.text = "Expand"
            isExpanded = false
        }
        else{
            TransitionManager.beginDelayedTransition(card, TransitionSet().apply {
                addTransition(ChangeBounds())
                addTransition(Fade())
                duration=500
            })


            this.layOutHiddenInfo.visibility = View.VISIBLE
            btnExpand.text = "Collapse"
            isExpanded = true
        }
    }

    fun bind(city: CityInfo, weather: CityWeather?) {
        txtCityName.text = city.name

        // Проверяем, если есть информация о погоде
        // Показываем погодный интерфейс
        if (weather != null) this.bindWeatherInfo(weather)

        itemView.setOnClickListener {
            this.card.isChecked = !this.card.isChecked
            listener.onCheckCity(city)
            listener.onCityItemClick(city)
            card.invalidate()

        }

        btnExpand.setOnClickListener{
            expand()
        }
    }

    private fun bindWeatherInfo(weather: CityWeather){
        txtTemperature.text = weather.main.temp.toString()
            .plus(" \u2103")

        txtTempDescription.text=weather.printTemperatureLine()

        val imageStr = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
        Glide.with(this.itemView)
            .load(imageStr)
            .centerCrop()
            .into(imgWeather);

        //val forecast = weather.weather
        //val tu = forecast[1].description
    }

}