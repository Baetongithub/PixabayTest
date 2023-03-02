package com.example.pixabaytest.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pixa_remote_keys_table")
data class PixaRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
) {
}