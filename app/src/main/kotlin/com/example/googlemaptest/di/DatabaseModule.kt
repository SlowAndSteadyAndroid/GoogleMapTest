package com.example.googlemaptest.di

import android.content.Context
import androidx.room.Room
import com.example.googlemaptest.room.allergie.AllergieDao
import com.example.googlemaptest.room.allergie.AllergieDatabase
import com.example.googlemaptest.room.bookmark.BookmarkDao
import com.example.googlemaptest.room.bookmark.BookmarkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAllergieDao(allergieDatabase: AllergieDatabase): AllergieDao {
        return allergieDatabase.allergieDao()
    }

    @Singleton
    @Provides
    fun provideAllergie(@ApplicationContext appContext: Context): AllergieDatabase {
        return Room.databaseBuilder(
            appContext,
            AllergieDatabase::class.java,
            "allergie_table"
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    fun provideBookmarkDao(bookmarkDatabase: BookmarkDatabase): BookmarkDao {
        return bookmarkDatabase.bookmarkDao()
    }

    @Singleton
    @Provides
    fun provideBookmark(@ApplicationContext appContext: Context): BookmarkDatabase {
        return Room.databaseBuilder(
            appContext,
            BookmarkDatabase::class.java,
            "bookmark_table"
        ).fallbackToDestructiveMigration()
            .build()
    }



}