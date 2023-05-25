package com.kotlinweather.adapters


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kotlinweather.R
import com.kotlinweather.databinding.VItemBinding
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather

class AutoCompleteAdapter(private var data: MutableMap<CityInfo, CityWeather?>) :
    Adapter<AutoCompleteViewHolder>() {
    private var cardClickListener: OnCardClickListener? = null
    private var forecastClickListener: OnForecastClickListener? = null

    fun setOnCardClickListener(settable: OnCardClickListener) {
        cardClickListener = settable
    }

    fun setOnForecastClickListener(settable: OnForecastClickListener) {
        forecastClickListener = settable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.v_item, parent, false)
        return AutoCompleteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        holder.bind(data.keys.elementAt(position), data.values.elementAt(position))

        // Обработчик нажатия на корневой cardView
        holder.binding.root.setOnClickListener {
            cardClickListener?.onCardClick(data.keys.elementAt(position))
        }

        // Обработчик нажатия на кнопку прогноз
        holder.binding.btnShowForecast.setOnClickListener {
            forecastClickListener?.onForecastClick(data.keys.elementAt(position))
        }
    }

    override fun getItemCount(): Int = data.size
}


class AutoCompleteViewHolder(item: View) : ViewHolder(item) {
    val binding = VItemBinding.bind(itemView)
    var iconUrl: String = "https://openweathermap.org/img/wn/"

    fun bind(city: CityInfo, weather: CityWeather?) {
        binding.lblSearchCityAutocomplete.text = city.name
        binding.lblCityCurrentTemperature.text =
            String().plus("Feels like ${weather?.main?.feels_like}")

        val icon: String? = weather?.weather?.getOrNull(0)?.icon
        if (!icon.isNullOrEmpty()) {
            val iconString = iconUrl.plus(icon).plus("@2x.png")

            val dr = object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.btnShowForecast.icon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            }

            Glide.with(binding.root.context)
                .asDrawable()
                .load(iconString)
                .into(dr)
        }
    }
}


fun interface OnCardClickListener {
    fun onCardClick(city: CityInfo)
}

fun interface OnForecastClickListener {
    fun onForecastClick(city: CityInfo)
}