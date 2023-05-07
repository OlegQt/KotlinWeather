package com.kotlinweather.presentation

import com.kotlinweather.http.Cities

interface OnCityClick {
    val type:Int
    fun onFoundCityClick(city:Cities)
}