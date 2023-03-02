package com.example.pixabaytest.data.mappers

interface DataMapper<T> {
    fun mapToDomain(): T
}