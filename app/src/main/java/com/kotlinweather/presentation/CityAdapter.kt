package com.kotlinweather.presentation

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kotlinweather.http.Cities
import com.kotlinweather.http.CityWeather

class CityAdapter(val data: MutableMap<Cities, CityWeather?>) :
    RecyclerView.Adapter<CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = data.size
}


class CityViewHolder(item:View):ViewHolder(item){

    fun bind(){

    }

}