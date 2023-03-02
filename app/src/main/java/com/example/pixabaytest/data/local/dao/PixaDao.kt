package com.example.pixabaytest.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pixabaytest.data.local.model.HitEntity

@Dao
interface PixaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(hitEntity: List<HitEntity>)

    @Query("DELETE FROM hit_entity")
    suspend fun deleteAllImages()

    @Query("SELECT * FROM hit_entity")
    fun getImages(): PagingSource<Int, HitEntity>
}