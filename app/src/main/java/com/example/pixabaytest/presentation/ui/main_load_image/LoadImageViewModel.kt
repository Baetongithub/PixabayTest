package com.example.pixabaytest.presentation.ui.main_load_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixabaytest.domain.model.Hit
import com.example.pixabaytest.domain.usecases.FindImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoadImageViewModel @Inject constructor(
    private val findImageUseCase: FindImageUseCase
) : ViewModel() {

    private var currentSearchResult: Flow<PagingData<Hit>>? = null

    fun findImage(keyWord: String): Flow<PagingData<Hit>> {
        val lastResult = currentSearchResult
        if (lastResult != null) {
            return lastResult
        }

        val newResult = findImageUseCase.findImage(keyWord)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}