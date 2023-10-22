package com.example.googlemaptest.room.allergie

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AllergieEntity::class], version = 1)
abstract class AllergieDatabase : RoomDatabase(){
    abstract fun allergieDao(): AllergieDao
}