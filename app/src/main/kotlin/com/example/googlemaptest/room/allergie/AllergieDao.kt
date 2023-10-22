package com.example.googlemaptest.room.allergie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AllergieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerAllergie(allergieEntity: AllergieEntity): Long

    @Query("SELECT * FROM allergie_table")
    fun getAll(): Flow<List<AllergieEntity>>

    @Delete
    suspend fun deleteAllergie(allergieEntity: AllergieEntity)
}