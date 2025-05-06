package com.example.laboration1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.laboration1.data.local.dao.MovieDao
import com.example.laboration1.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],   // ← måste inkludera entiteten
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
