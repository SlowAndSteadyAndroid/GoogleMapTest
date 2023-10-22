package com.example.googlemaptest.room.bookmark

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerBookmark(bookmarkEntity: BookmarkEntity): Long

    @Delete
    suspend fun deleteBookmark(bookmarkEntity: BookmarkEntity)

    @Query("SELECT * FROM bookmark_table")
    fun getAll(): Flow<List<BookmarkEntity>>
}