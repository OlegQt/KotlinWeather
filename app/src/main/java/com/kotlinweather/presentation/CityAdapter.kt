package com.kotlinweather.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlinweather.R
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather

class CityAdapter(private val data: MutableMap<CityInfo, CityWeather?>, val listener: OnCityClick) :
    RecyclerView.Adapter<CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)
        return CityViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(data.keys.elementAt(position), data.values.elementAt(position))
    }

    override fun getItemCount(): Int = data.size
}

