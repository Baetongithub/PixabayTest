package com.example.pixabaytest.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixabaytest.domain.model.Hit
import com.example.pixabaytest.domain.usecases.FindImageUseCase
import com.example.pixabaytest.domain.usecases.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class LoadImageViewModel @Inject constructor(
    private val findImageUseCase: FindImageUseCase,
    private val getImagesUseCase: GetImagesUseCase
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

    val getImages = getImagesUseCase.get()
}