package com.kotlinweather.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kotlinweather.R
import com.kotlinweather.databinding.VItemBinding

class AutoCompleteAdapter(private val data: Set<String>) : Adapter<AutoCompleteViewHolder>() {
    lateinit var listener: OnCardClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.v_item, parent, false)

        val binding:VItemBinding = VItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AutoCompleteViewHolder(itemView,binding)
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        holder.bind(data.elementAt(position))
        holder.binding.lblSearchCityAutocomplete.text ="aaa"
        holder.binding.lblSearchCityAutocomplete.setOnClickListener {
            listener.onCardClick(data.elementAt(position))
        }
    }

    override fun getItemCount(): Int = data.size
}

class AutoCompleteViewHolder(item: View,val binding: VItemBinding) : ViewHolder(item) {

    //val txt = item.findViewById<TextView>(R.id.lbl_search_city_autocomplete)

    fun bind(str: String) {
        binding.lblSearchCityAutocomplete.text = "ddd"
        //txt.text=str
    }
}

interface OnCardClickListener {
    fun onCardClick(str: String)
}