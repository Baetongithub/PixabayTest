package com.example.pixabaytest.presentation.ui.main_load_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixabaytest.data.model.Hit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoadImageViewModel @Inject constructor(
    private val loadImageRepo: LoadImageRepository
) : ViewModel() {

    private var keyWord: String? = null
    private var currentSearchResult: Flow<PagingData<Hit>>? = null

    fun searchRepos(keyWord: String): Flow<PagingData<Hit>> {
        val lastResult = currentSearchResult
        if (keyWord == this.keyWord && lastResult != null) {
            return lastResult
        }
        this.keyWord = keyWord
        val newResult = loadImageRepo.getPagedImages(keyWord)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}