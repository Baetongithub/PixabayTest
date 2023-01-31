package com.example.pixabaytest.ui.main_load_image.load_state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pixabaytest.databinding.ItemLoadStateBinding

class FooterLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FooterViewHolder>() {
    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterViewHolder {
        val itemPagingFooterBinding =
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FooterViewHolder(itemPagingFooterBinding, retry)
    }

}

class FooterViewHolder(
    private val vb: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(vb.root) {

    init {
        vb.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            vb.tvLoadStateMessage.text = loadState.error.localizedMessage
        }
        vb.progressBar.visibility = toVisibility(loadState is LoadState.Loading)
        vb.btnRetry.visibility = toVisibility(loadState !is LoadState.Loading)
        vb.tvLoadStateMessage.visibility = toVisibility(loadState !is LoadState.Loading)
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}