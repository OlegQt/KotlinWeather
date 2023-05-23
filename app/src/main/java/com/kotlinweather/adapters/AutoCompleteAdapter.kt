package com.kotlinweather.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kotlinweather.R
import com.kotlinweather.databinding.VItemBinding
import com.kotlinweather.http.CityInfo
import com.kotlinweather.http.CityWeather

class AutoCompleteAdapter(private var data: MutableMap<CityInfo,CityWeather?>) : Adapter<AutoCompleteViewHolder>() {
    var listener: OnCardClickListener? = null

    fun setOnCardClickListener(settable: OnCardClickListener){
        listener = settable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.v_item,parent,false)
        return AutoCompleteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        holder.bind(data.keys.elementAt(position),data.values.elementAt(position))

        holder.binding.root.setOnClickListener {
            listener?.onCardClick(data.keys.elementAt(position))
        }
    }

    override fun getItemCount(): Int = data.size
}






class AutoCompleteViewHolder(item: View) : ViewHolder(item) {
    val binding = VItemBinding.bind(itemView)
    fun bind(city: CityInfo,weather:CityWeather?) {
        binding.lblSearchCityAutocomplete.text = city.name
    }
}




fun interface OnCardClickListener {
    fun onCardClick(city: CityInfo)
}