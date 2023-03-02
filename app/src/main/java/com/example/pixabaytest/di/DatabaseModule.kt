package com.example.pixabaytest.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.pixabaytest.data.local.PixaDatabase
import com.example.pixabaytest.data.remote.network.PixaAPI
import com.example.pixabaytest.data.repository.GetImageRepository
import com.example.pixabaytest.domain.repository.ImageRepository
import com.example.pixabaytest.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PixaDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = PixaDatabase::class.java,
            name = Constants.PIXA_DB_NAME
        ).build()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideGetImagesRepository(pixaAPI: PixaAPI, pixaDatabase: PixaDatabase): GetImageRepository {
        return GetImageRepository(pixaAPI = pixaAPI, pixaDatabase = pixaDatabase)
    }
}