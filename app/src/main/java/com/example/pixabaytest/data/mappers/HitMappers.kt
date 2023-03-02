package com.example.pixabaytest.data.mappers

import com.example.pixabaytest.data.local.model.HitEntity
import com.example.pixabaytest.domain.model.Hit

fun Hit.toHitEntity() = HitEntity(
    id,
    comments,
    downloads,
    imageHeight,
    imageSize,
    imageWidth,
    largeImageURL,
    likes,
    pageURL,
    previewHeight,
    previewURL,
    previewWidth,
    tags,
    type,
    user,
    userImageURL,
    user_id,
    views,
    webformatHeight,
    webformatURL,
    webformatWidth
)

fun HitEntity.toHit() = Hit(
    id,
    comments,
    downloads,
    imageHeight,
    imageSize,
    imageWidth,
    largeImageURL,
    likes,
    pageURL,
    previewHeight,
    previewURL,
    previewWidth,
    tags,
    type,
    user,
    userImageURL,
    user_id,
    views,
    webformatHeight,
    webformatURL,
    webformatWidth
)