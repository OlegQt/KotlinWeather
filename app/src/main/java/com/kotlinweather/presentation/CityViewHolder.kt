package com.kotlinweather.presentation

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kotlinweather.R
import com.kotlinweather.http.Cities

class CityViewHolder(item: View, private val listener:OnCityClick) : RecyclerView.ViewHolder(item) {
    private val card: MaterialCardView = item.findViewById(R.id.card)
    private val txtCityName: TextView = item.findViewById(R.id.txt_city_name)
    private val txtCountry: TextView = item.findViewById(R.id.txt_secondary_info)
    private val txtLat: TextView = item.findViewById(R.id.txt_support_info)

    init {
        if (listener.type==0){
            val colorSalmon = ContextCompat.getColor(item.context,R.color.salmon)
            card.setCardBackgroundColor(colorSalmon)
            card.isCheckable=true
            card.checkedIconGravity = MaterialCardView.CHECKED_ICON_GRAVITY_BOTTOM_END
            txtCityName.setBackgroundColor(colorSalmon)
        }
    }

    private fun Cities.print():String{
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
            this.card.isChecked = !this.card.isChecked
            card.invalidate()

        }
    }

}