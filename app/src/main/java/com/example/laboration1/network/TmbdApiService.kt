package com.example.laboration1.network

import com.example.laboration1.model.Movie
import com.example.laboration1.network.model.ApiMovie
import com.example.laboration1.network.model.GenreResponse
import com.example.laboration1.network.model.MovieDetailsResponse
import com.example.laboration1.network.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsResponse

}

fun ApiMovie.toMovie(genreLookup: Map<Int, String>): Movie {
    return Movie(
        id = id,
        title = title,
        genres = genreIds.mapNotNull { genreLookup[it] },
        homepage = "", // will add later
        imdbId = "",   // will add later
        posterPath = posterPath ?: ""
    )
}



