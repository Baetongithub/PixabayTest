package com.example.pixabaytest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pixabaytest.data.local.model.PixaRemoteKeys

@Dao
interface PixaRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(remoteKeys: List<PixaRemoteKeys>)

    @Query("DELETE FROM pixa_remote_keys_table")
    suspend fun deleteAllRemoteKeys()

    @Query("SELECT * FROM pixa_remote_keys_table WHERE id=:id")
    suspend fun getRemoteKeys(id: String):PixaRemoteKeys
}