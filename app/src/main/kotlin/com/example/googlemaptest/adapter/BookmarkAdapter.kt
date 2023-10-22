package com.example.googlemaptest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaptest.databinding.ItemPlaceBinding
import com.example.googlemaptest.room.bookmark.BookmarkEntity

class BookmarkAdapter(
    val onDelete: (BookmarkEntity) -> Unit,
    val onRoute : (BookmarkEntity) -> Unit
) :
    ListAdapter<BookmarkEntity, BookmarkViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BookmarkEntity>() {
            override fun areItemsTheSame(
                oldItem: BookmarkEntity,
                newItem: BookmarkEntity
            ): Boolean {
                return oldItem.placeId == newItem.placeId
            }

            override fun areContentsTheSame(
                oldItem: BookmarkEntity,
                newItem: BookmarkEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position), onDelete, onRoute)
    }
}

class BookmarkViewHolder(private val binding: ItemPlaceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: BookmarkEntity, onDelete: (BookmarkEntity) -> Unit, onRoute : (BookmarkEntity) -> Unit) {
        binding.entity = entity

        binding.root.setOnLongClickListener {
            onDelete(entity)
            true
        }

        binding.root.setOnClickListener {
            onRoute(entity)
        }
    }
}