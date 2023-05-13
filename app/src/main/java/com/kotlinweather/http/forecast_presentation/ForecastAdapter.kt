package com.kotlinweather.http.forecast_presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.kotlinweather.R
import com.kotlinweather.http.CityForecast
import com.kotlinweather.sharedprefs.WeatherApp

class ForecastAdapter(private val forecast:MutableList<CityForecast.WeatherListItem>):Adapter<ForecastViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val item:View = LayoutInflater.from(parent.context).inflate(R.layout.city_forecast_item,parent,false)
        return ForecastViewHolder(item)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecast.get(position))
    }

    override fun getItemCount(): Int = forecast.size
}

class ForecastViewHolder(itemView:View):ViewHolder(itemView){
    private val txtForecastDate:TextView = itemView.findViewById(R.id.forecast_date)
    private val txtTemperature:TextView = itemView.findViewById(R.id.forecast_temperature)
    private val imgWeather:ImageView = itemView.findViewById(R.id.forecast_image)

    fun bind(forecast:CityForecast.WeatherListItem){
        txtForecastDate.text = WeatherApp.instance.getDateFromLong("E d MMM",forecast.dt)
        txtTemperature.text = forecast.main.feels_like.toString()

        val imageStr = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png"
        Glide.with(this.itemView)
            .load(imageStr)
            .centerCrop()
            .into(imgWeather);
    }
}