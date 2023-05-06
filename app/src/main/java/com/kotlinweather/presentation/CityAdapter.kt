package com.kotlinweather.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kotlinweather.R
import com.kotlinweather.http.Cities
import com.kotlinweather.http.CityWeather

class CityAdapter(private val data: MutableMap<Cities, CityWeather?>) :
    RecyclerView.Adapter<CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)
        return CityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(data.keys.elementAt(position))
    }

    override fun getItemCount(): Int = data.size
}

class CityViewHolder(item: View) : ViewHolder(item) {
    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtCountry: TextView = item.findViewById(R.id.txt_secondary_info)
    private val txtLat: TextView = item.findViewById(R.id.txt_support_info)

    fun bind(city: Cities) {
        txtCityName.text = city.name
        txtCountry.text = city.country
        txtLat.text="${city.lat}  |  ${city.lon}"
    }

}