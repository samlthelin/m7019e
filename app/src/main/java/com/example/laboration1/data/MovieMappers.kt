package com.example.laboration1.data

import androidx.room.PrimaryKey
import com.example.laboration1.data.local.MovieDetailEntity
import com.example.laboration1.data.local.MovieEntity
import com.example.laboration1.model.Movie
import com.example.laboration1.network.model.ApiMovie

// for homescreen
fun ApiMovie.toEntity(viewType: String, genreMap: Map<Int, String>): MovieEntity {
    val genreNames = genreIds.mapNotNull { genreMap[it] }
    return MovieEntity(
        id = id,
        title = title,
        genres = genreNames.joinToString(","),
        homepage = "", // not available here
        imdbId = "",   // not available here
        posterPath = posterPath ?: "",
        viewType = viewType
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genres.split(","),
        homepage = homepage,
        imdbId = imdbId,
        posterPath = posterPath
    )
}

//for detailscreen
fun Movie.toDetailEntity(): MovieDetailEntity {
    return MovieDetailEntity(
        id = id,
        title = title,
        genres = genres.joinToString(","),
        homepage = homepage, // not available here
        imdbId = imdbId,   // not available here
        posterPath = posterPath
    )
}

fun MovieDetailEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genres.split(","),
        homepage = homepage,
        imdbId = imdbId,
        posterPath = posterPath
    )
}

fun ApiMovie.toMovie(genreMap: Map<Int, String>): Movie {
    val genreNames = genreIds.mapNotNull { genreMap[it] }
    return Movie(
        id = id,
        title = title,
        genres = genreNames,
        homepage = "",
        imdbId = "",
        posterPath = posterPath ?: ""
    )
}