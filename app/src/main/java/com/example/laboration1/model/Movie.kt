package com.example.laboration1.model

data class Movie(
    val id: Int,
    val title: String,
    val genres: List<String>,
    val homepage: String,
    val imdbId: String,
    val posterPath: String
)