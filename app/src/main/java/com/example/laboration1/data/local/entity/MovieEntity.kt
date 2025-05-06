package com.example.laboration1.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")          // ← VIKTIGT
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val category: String               // t.ex. "popular", "top_rated"
)
