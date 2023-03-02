package com.example.pixabaytest.presentation.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pixabaytest.databinding.ItemImagesBinding
import com.example.pixabaytest.domain.model.Hit

class ImagePagerAdapter(private val onItemClick: (hit: Hit) -> Unit) :
    PagingDataAdapter<Hit, ImagePagerAdapter.PixaViewHolder>(UsersDiffCallback()) {

    override fun onBindViewHolder(holder: PixaViewHolder, position: Int) {
        if (getItem(position) != null) {
            holder.itemView.setOnClickListener { onItemClick(getItem(position)!!) }
            getItem(position)?.let { holder.bind(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixaViewHolder {
        return PixaViewHolder(
            ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class PixaViewHolder(private val binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hit: Hit) {
            binding.images.load(hit.webformatURL)
        }
    }

    private class UsersDiffCallback : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }
    }
}