package com.example.googlemaptest.room.bookmark

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookmarkEntity::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase(){
    abstract fun bookmarkDao(): BookmarkDao
}