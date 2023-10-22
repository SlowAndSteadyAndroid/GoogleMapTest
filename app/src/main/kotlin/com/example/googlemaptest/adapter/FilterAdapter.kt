package com.example.googlemaptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaptest.databinding.ItemFilterBinding
import com.example.googlemaptest.room.allergie.AllergieEntity

class FilterAdapter(val onDelete: (AllergieEntity) -> Unit) : ListAdapter<AllergieEntity,FilterViewHolder >(diffUtil){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position),onDelete)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AllergieEntity>() {
            override fun areItemsTheSame(
                oldItem: AllergieEntity,
                newItem: AllergieEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AllergieEntity,
                newItem: AllergieEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}


class FilterViewHolder(private val binding: ItemFilterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AllergieEntity, onDelete: (AllergieEntity) -> Unit) {
        binding.title = item.name
        binding.ivClear.setOnClickListener {
            onDelete(item)
        }
    }
}