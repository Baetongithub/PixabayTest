package com.example.pixabaytest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pixabaytest.data.local.dao.PixaDao
import com.example.pixabaytest.data.local.dao.PixaRemoteKeysDao
import com.example.pixabaytest.data.local.model.HitEntity
import com.example.pixabaytest.data.local.model.PixaRemoteKeys

@Database(entities = [HitEntity::class, PixaRemoteKeys::class], version = 1)
abstract class PixaDatabase : RoomDatabase() {

    abstract fun getPixaDao(): PixaDao
    abstract fun getPixaRemoteKeysDao(): PixaRemoteKeysDao
}