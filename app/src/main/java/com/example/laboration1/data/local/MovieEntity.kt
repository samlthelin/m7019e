package com.example.laboration1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val genres: String, // stored as CSV, e.g. "Action,Drama"
    val homepage: String,
    val imdbId: String,
    val posterPath: String,
    val viewType: String // e.g., "popular" or "top_rated"
)