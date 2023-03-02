package com.example.pixabaytest.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pixabaytest.data.local.PixaDatabase
import com.example.pixabaytest.data.local.model.HitEntity
import com.example.pixabaytest.data.local.model.PixaRemoteKeys
import com.example.pixabaytest.data.remote.network.PixaAPI

@ExperimentalPagingApi
class PixaRemoteMediatorSource(
    private val pixaAPI: PixaAPI,
    private val pixaDatabase: PixaDatabase
) : RemoteMediator<Int, HitEntity>() {

    private val pixaDao = pixaDatabase.getPixaDao()
    private val pixaRemoteKeysDao = pixaDatabase.getPixaRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HitEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: START_PAGE
                }
                PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
                APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }
            val response = pixaAPI.getImages(page = currentPage)
            val endOfPaginationReached = response.hits.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            pixaDatabase.withTransaction {
                if (loadType == REFRESH) {
                    pixaDao.deleteAllImages()
                    pixaRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.hits.map { hit ->
                    PixaRemoteKeys(
                        id = hit.id.toString(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                pixaRemoteKeysDao.insertAllRemoteKeys(remoteKeys = keys)

                pixaDao.insertImages(response.hits)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, HitEntity>
    ): PixaRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                pixaRemoteKeysDao.getRemoteKeys(id = id.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, HitEntity>
    ): PixaRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pixaImages ->
                pixaRemoteKeysDao.getRemoteKeys(id = pixaImages.id.toString())
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, HitEntity>
    ): PixaRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { hit ->
                pixaRemoteKeysDao.getRemoteKeys(id = hit.id.toString())
            }
    }

    private companion object {
        const val START_PAGE = 1
    }
}