package com.example.pixabaytest.presentation.ui.main_load_image

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pixabaytest.databinding.ItemImagesBinding
import com.example.pixabaytest.domain.model.Hit

class ImagePagerAdapter(private val onItemClick: (hit: Hit) -> Unit) :
    PagingDataAdapter<Hit, ImagePagerAdapter.PixaViewHolder>(UsersDiffCallback()) {

    override fun onBindViewHolder(holder: PixaViewHolder, position: Int) {
        val currentImage = getItem(position)
        with(holder.binding) {
            currentImage?.webformatURL?.let { loadUserPhoto(images, it) }
            images.setOnClickListener {
                if (currentImage != null) {
                    onItemClick(currentImage)
                }
            }
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

    private fun loadUserPhoto(imageView: ImageView, url: String) {
        if (url.isNotEmpty()) {
            imageView.load(url)
        }
    }

    inner class PixaViewHolder(
        val binding: ItemImagesBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

// ---

class UsersDiffCallback : DiffUtil.ItemCallback<Hit>() {
    override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
        return oldItem == newItem
    }
}