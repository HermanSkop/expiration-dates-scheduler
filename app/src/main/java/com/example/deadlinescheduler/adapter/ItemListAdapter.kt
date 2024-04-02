package com.example.deadlinescheduler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deadlinescheduler.databinding.ItemBinding
import com.example.deadlinescheduler.model.Item

class ItemView(private val itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
    fun onBind(item: Item) {
        with(itemBinding) {
            name.text = item.name
            quantity.text = item.number.toString()
            expirationDate.text = item.expirationDate.toString()
        }
    }
}

class ItemListAdapter : RecyclerView.Adapter<ItemView>() {
    var items: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)
        return ItemView(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        holder.onBind(items[position])
    }
}