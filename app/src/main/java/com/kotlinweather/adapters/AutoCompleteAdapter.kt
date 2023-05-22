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
    var listener: OnCardClickListener? = null

    fun setListenerBehaviour(settable: OnCardClickListener){
        listener = settable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.v_item,parent,false)
        return AutoCompleteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        holder.bind(data.elementAt(position))

        holder.binding.root.setOnClickListener {
            listener?.onCardClick(data.elementAt(position))
        }
    }

    override fun getItemCount(): Int = data.size
}






class AutoCompleteViewHolder(item: View) : ViewHolder(item) {
    val binding = VItemBinding.bind(itemView)
    fun bind(str: String) {
        binding.lblSearchCityAutocomplete.text = str
    }
}




fun interface OnCardClickListener {
    fun onCardClick(str: String)
}