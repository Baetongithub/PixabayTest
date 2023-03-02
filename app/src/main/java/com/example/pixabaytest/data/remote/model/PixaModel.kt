package com.example.pixabaytest.data.remote.model

import com.example.pixabaytest.data.local.model.HitEntity

data class PixaModel(
    val hits: List<HitEntity>,
    val total: Int,
    val totalHits: Int
)