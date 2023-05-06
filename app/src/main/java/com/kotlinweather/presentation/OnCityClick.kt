package com.kotlinweather.presentation

import com.kotlinweather.http.Cities

interface OnCityClick {
    fun onFoundCityClick(city:Cities)
}