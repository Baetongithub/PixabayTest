package com.example.pixabaytest.data.model

data class PixaModel(
    val hits: List<HitEntity>,
    val total: Int,
    val totalHits: Int
)