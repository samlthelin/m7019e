package com.example.laboration1.data.repository

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.laboration1.Workers.CacheMoviesWorker
import com.example.laboration1.data.local.MovieDatabase
import com.example.laboration1.data.toDetailEntity
import com.example.laboration1.data.toEntity
import com.example.laboration1.data.toMovie
import com.example.laboration1.model.Movie
import com.example.laboration1.network.TmdbClient
import com.example.laboration1.network.model.ApiReview
import com.example.laboration1.network.model.ApiVideo
import com.example.laboration1.url.Secrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val context: Context) {

    suspend fun fetchMovies(viewType: String): Result<Pair<List<Movie>, Map<String, List<Movie>>>> {
        return withContext(Dispatchers.IO) {
            val db = MovieDatabase.getDatabase(context)
            val dao = db.movieDao()

            try {
                val cached = dao.getMoviesByViewType(viewType).map { it.toMovie() }

                val groupedCached = cached
                    .flatMap { movie -> movie.genres.map { genre -> genre to movie } }
                    .sortedBy { it.first }
                    .groupBy({ it.first }, { it.second })
                    .mapValues { (_, movies) -> movies.sortedBy { it.title } }

                // Fetch from API
                val genreResponse = TmdbClient.api.getGenres(Secrets.API_KEY)
                val genreMap = genreResponse.genres.associateBy({ it.id }, { it.name })

                val movieResponse = when (viewType) {
                    "top_rated" -> TmdbClient.api.getTopRatedMovies(Secrets.API_KEY)
                    else -> TmdbClient.api.getPopularMovies(Secrets.API_KEY)
                }

                val movieList = movieResponse.results.map { it.toMovie(genreMap) }

                val grouped = movieList
                    .flatMap { movie -> movie.genres.map { genre -> genre to movie } }
                    .sortedBy { it.first }
                    .groupBy({ it.first }, { it.second })
                    .mapValues { (_, movies) -> movies.sortedBy { it.title } }

                val entities = movieResponse.results.map { it.toEntity(viewType, genreMap) }
                dao.clearMoviesExcept(viewType)
                dao.insertMovies(entities)

                Result.success(Pair(movieList, grouped))
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    suspend fun fetchMovieDetails(movieId: Int): Result<Movie> {
        return withContext(Dispatchers.IO) {
            val db = MovieDatabase.getDatabase(context)
            val dao = db.movieDao()

            val cached = dao.getMovieDetailById(movieId)
            if (cached != null) {
                return@withContext Result.success(cached.toMovie())
            }

            try {
                val response = TmdbClient.api.getMovieDetails(movieId, Secrets.API_KEY)
                val movie = Movie(
                    id = response.id,
                    title = response.title,
                    genres = response.genres.map { it.name },
                    homepage = response.homepage,
                    imdbId = response.imdbId,
                    posterPath = response.posterPath
                )
                dao.insertMovieDetail(movie.toDetailEntity())
                Result.success(movie)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    suspend fun fetchReviews(movieId: Int): Result<List<ApiReview>> {
        return try {
            val response = TmdbClient.api.getReviews(movieId, Secrets.API_KEY)
            Result.success(response.results)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun fetchVideos(movieId: Int): Result<List<ApiVideo>> {
        return try {
            val response = TmdbClient.api.getVideos(movieId, Secrets.API_KEY)
            val videos = response.results.filter { it.site == "YouTube" && it.type == "Trailer" }
            Result.success(videos)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun scheduleMovieCaching(sortType: String) {
        val data = workDataOf("sort_type" to sortType)

        val request = OneTimeWorkRequestBuilder<CacheMoviesWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "cache_movies",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}