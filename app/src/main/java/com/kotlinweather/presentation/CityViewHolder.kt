package com.kotlinweather.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlinweather.R
import com.kotlinweather.http.Cities

class CityViewHolder(item: View, val listener:OnCityClick) : RecyclerView.ViewHolder(item) {
    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtCountry: TextView = item.findViewById(R.id.txt_secondary_info)
    private val txtLat: TextView = item.findViewById(R.id.txt_support_info)


    fun Cities.print():String{
        val sLat = String.format("%.3f",this.lat)
        val sLon = String.format("%.3f",this.lon)
        return sLat.plus(" | ").plus(sLon)
    }

    fun bind(city: Cities) {
        txtCityName.text = city.name
        txtCountry.text = city.country
        txtLat.text = city.print()

        itemView.setOnClickListener {
            this.listener.onFoundCityClick(city)
        }
    }

}