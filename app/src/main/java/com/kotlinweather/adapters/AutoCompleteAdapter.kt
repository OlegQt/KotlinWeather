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
    lateinit var binding: VItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.v_item,parent,false)
        return AutoCompleteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        holder.bind(data.elementAt(position))
        // binding.lblSearchCityAutocomplete.text="A"

        /*holder.binding.lblSearchCityAutocomplete.setOnClickListener {
            listener.onCardClick(data.elementAt(position))
        }*/
    }

    override fun getItemCount(): Int = data.size
}

class AutoCompleteViewHolder(item: View) : ViewHolder(item) {
    val binding = VItemBinding.bind(itemView)
    fun bind(str: String) {
        binding.lblSearchCityAutocomplete.text = str
    }
}

interface OnCardClickListener {
    fun onCardClick(str: String)
}