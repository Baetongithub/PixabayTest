package com.example.pixabaytest.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pixabaytest.data.mappers.DataMapper
import com.example.pixabaytest.domain.model.Hit

@Entity(tableName = "hit_entity")
data class HitEntity(

    @PrimaryKey
    val id: Int,

    val comments: Int,
    val downloads: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    val user_id: Int,
    val views: Int,
    val webformatHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int
) : DataMapper<Hit> {
    override fun mapToDomain(): Hit {
        return Hit(
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
    }
}