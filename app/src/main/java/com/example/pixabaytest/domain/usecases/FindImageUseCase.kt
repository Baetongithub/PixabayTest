package com.example.pixabaytest.domain.usecases

import com.example.pixabaytest.domain.repository.ImageRepository
import javax.inject.Inject

class FindImageUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    fun findImage(keyWord: String) = repository.searchImages(keyWord = keyWord)
}