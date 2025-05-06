package com.example.laboration1.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("results")
    val results: List<ApiMovie>
)

@Serializable
data class ApiMovie(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>
)


@Serializable
data class GenreResponse(
    val genres: List<Genre>
)

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class MovieDetailsResponse(
    val id: Int,
    val title: String,
    val genres: List<Genre>,
    val homepage: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("imdb_id") val imdbId: String
)

