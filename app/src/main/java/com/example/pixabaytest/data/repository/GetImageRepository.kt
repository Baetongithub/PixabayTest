package com.example.pixabaytest.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pixabaytest.data.local.PixaDatabase
import com.example.pixabaytest.data.local.model.HitEntity
import com.example.pixabaytest.data.paging.PixaRemoteMediatorSource
import com.example.pixabaytest.data.remote.network.PixaAPI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class GetImageRepository @Inject constructor(
    private val pixaAPI: PixaAPI,
    private val pixaDatabase: PixaDatabase
) {

    fun getImages(): Flow<PagingData<HitEntity>> {
        val pagingSourceFactory = { pixaDatabase.getPixaDao().getImages() }
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = PixaRemoteMediatorSource(pixaAPI, pixaDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    private companion object {
        const val ITEMS_PER_PAGE = 10
    }
}