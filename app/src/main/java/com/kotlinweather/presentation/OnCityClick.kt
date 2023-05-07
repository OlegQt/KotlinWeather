package com.kotlinweather.presentation

import com.kotlinweather.http.CityInfo
import java.text.FieldPosition

interface OnCityClick {
    val type:Int
    fun onCityItemClick(city:CityInfo){}
    fun onCheckCity(city:CityInfo){}
}