package com.example.laboration1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val genres: String, // stored as CSV, e.g. "Action,Drama"
    val homepage: String,
    val imdbId: String,
    val posterPath: String,
)