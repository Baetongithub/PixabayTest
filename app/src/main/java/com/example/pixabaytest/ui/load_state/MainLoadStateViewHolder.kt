package com.example.pixabaytest.ui.load_state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.pixabaytest.R
import com.example.pixabaytest.databinding.ItemLoadStateBinding

class MainLoadStateViewHolder(
    private val itemBinding: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    itemBinding.root
) {
//     private val retry: Button = itemView.findViewById<Button>(R.id.retry_button)
//        .also { it.setOnClickListener { retry.invoke() } }

    init {
        itemBinding.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            itemBinding.tvLoadStateMessage.text = loadState.error.localizedMessage
        }
        itemBinding.progressBar.visibility = toVisibility(loadState is LoadState.Loading)
        itemBinding.btnRetry.visibility = toVisibility(loadState !is LoadState.Loading)
        itemBinding.tvLoadStateMessage.visibility = toVisibility(loadState !is LoadState.Loading)
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MainLoadStateViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_load_state, parent, false)
            val binding = ItemLoadStateBinding.bind(view)
            return MainLoadStateViewHolder(binding, retry)
        }
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}