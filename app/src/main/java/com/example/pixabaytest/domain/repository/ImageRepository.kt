package com.example.pixabaytest.domain.repository

import androidx.paging.PagingData
import com.example.pixabaytest.domain.model.Hit
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getPagedImages(keyWord: String): Flow<PagingData<Hit>>
}