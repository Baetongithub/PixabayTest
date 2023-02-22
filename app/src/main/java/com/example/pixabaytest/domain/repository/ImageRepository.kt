package com.example.pixabaytest.domain

import androidx.paging.PagingData
import com.example.pixabaytest.data.model.Hit
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    fun getPagedImages(keyWord: String): Flow<PagingData<Hit>>
}