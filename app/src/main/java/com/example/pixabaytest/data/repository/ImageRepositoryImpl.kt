package com.example.pixabaytest.presentation.ui.main_load_image

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pixabaytest.data.remote.network.PixaAPI
import com.example.pixabaytest.data.remote.network.PixaPagingSource
import com.example.pixabaytest.data.model.Hit
import com.example.pixabaytest.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadImageRepository @Inject constructor(private val pixaAPI: PixaAPI) : ImageRepository {

    override fun getPagedImages(keyWord: String): Flow<PagingData<Hit>> {
        return Pager(
            pagingSourceFactory = { PixaPagingSource(pixaAPI, keyWord = keyWord) },
            config = PagingConfig(pageSize = 5, maxSize = 20)
        ).flow
    }
}