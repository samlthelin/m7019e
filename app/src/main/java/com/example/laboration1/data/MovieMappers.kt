package com.example.laboration1.data

import androidx.room.PrimaryKey
import com.example.laboration1.data.local.MovieDetailEntity
import com.example.laboration1.data.local.MovieEntity
import com.example.laboration1.model.Movie
import com.example.laboration1.network.model.ApiMovie

// for homescreen


fun ApiMovie.toEntity(viewType: String, genreMap: Map<Int, String>): MovieEntity {
    // convert the genre id from API to genre names using mapp
    val genreNames = genreIds.mapNotNull { genreMap[it] }

    return MovieEntity(
        id = id,
        title = title,
        genres = genreNames.joinToString(","),  // room needs flattened
        homepage = "",     // not included
        imdbId = "",       // not incl.
        posterPath = posterPath ?: "",
        viewType = viewType  // store which kind of list this movie belongs to ("popular", "top_rated")
    )
}


fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genres.split(","), // reverse actions from above: from string to list!
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
        homepage = homepage,
        imdbId = imdbId,
        posterPath = posterPath
    )
}

fun MovieDetailEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genres.split(","), //stored as string, split back 2 list
        homepage = homepage,
        imdbId = imdbId,
        posterPath = posterPath
    )
}

// this one was previously in viewmodel. we go from api-values to actual displayable movie! (use serialize)
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