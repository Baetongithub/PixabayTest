package com.example.pixabaytest.domain.usecases

import androidx.paging.ExperimentalPagingApi
import com.example.pixabaytest.data.repository.GetImageRepository
import javax.inject.Inject

@ExperimentalPagingApi
class GetImagesUseCase @Inject constructor(
    private val repository: GetImageRepository
) {

    fun get() = repository.getImages()
}