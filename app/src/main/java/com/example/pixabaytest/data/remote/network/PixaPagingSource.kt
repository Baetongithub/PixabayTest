package com.example.pixabaytest.data.remote.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixabaytest.data.mappers.toHit
import com.example.pixabaytest.domain.model.Hit
import retrofit2.HttpException
import java.io.IOException

class PixaPagingSource(
    private val pixaAPI: PixaAPI,
    private val keyWord: String
) : PagingSource<Int, Hit>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = pixaAPI.getImages(keyWord = keyWord, page = page)

            val hits = response.hits.map { it.toHit() }
            LoadResult.Page(
                data = hits,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (hits.isEmpty()) null else page.plus(1)
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}