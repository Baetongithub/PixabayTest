package com.example.pixabaytest.presentation.ui.load_state

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * Adapter which displays a loading spinner when `state = LoadState.Loading`, and an error
 * message and retry button when `state is LoadState.Error`.
 */
class MyLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MainLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        MainLoadStateViewHolder.create(parent, retry)

    override fun onBindViewHolder(holder: MainLoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}