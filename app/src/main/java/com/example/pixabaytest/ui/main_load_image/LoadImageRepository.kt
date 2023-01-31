package com.example.pixabaytest.ui.main_load_image

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pixabaytest.data.remote.network.PixaAPI
import com.example.pixabaytest.data.remote.network.PixaPagingSource
import com.example.pixabaytest.data.model.Hit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadImageRepository @Inject constructor(private val pixaAPI: PixaAPI) : RepoInterface {

    override fun getPagedImages(keyWord: String): Flow<PagingData<Hit>> {
        return Pager(
            pagingSourceFactory = { PixaPagingSource(pixaAPI, keyWord = keyWord) },
            config = PagingConfig(pageSize = 5)
        ).flow
    }
}