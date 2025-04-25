package com.example.laboration1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboration1.data.MovieRepository.movieList
import com.example.laboration1.model.Movie
import com.example.laboration1.network.TmdbClient
import com.example.laboration1.network.model.ApiReview
import com.example.laboration1.network.model.ApiVideo
import com.example.laboration1.network.toMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.laboration1.url.Secrets
import com.example.laboration1.network.model.MovieResponse
import com.example.laboration1.network.model.ReviewResponse
import com.example.laboration1.network.model.VideoResponse




class MovieViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _genreSections = MutableStateFlow<Map<String, List<Movie>>>(emptyMap())
    val genreSections: StateFlow<Map<String, List<Movie>>> = _genreSections


    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val genreResponse = TmdbClient.api.getGenres(Secrets.API_KEY)
                val genreMap = genreResponse.genres.associateBy({ it.id }, { it.name })

                val movieResponse = TmdbClient.api.getPopularMovies(Secrets.API_KEY)
                val movieList = movieResponse.results.map { it.toMovie(genreMap) }

                Log.d("MovieViewModel", "Fetched ${movieList.size} movies")

                _movies.value = movieList

                val grouped = movieList
                    .flatMap { movie -> movie.genres.map { genre -> genre to movie } }
                    .groupBy({ it.first }, { it.second })

                _genreSections.value = grouped
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = TmdbClient.api.getMovieDetails(movieId, Secrets.API_KEY)

                _selectedMovie.value = Movie(
                    id = response.id,
                    title = response.title,
                    genres = response.genres.map { it.name },
                    homepage = response.homepage,
                    imdbId = response.imdbId,
                    posterPath = response.posterPath
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _reviews = MutableStateFlow<List<ApiReview>>(emptyList())
    val reviews: StateFlow<List<ApiReview>> = _reviews

    private val _videos = MutableStateFlow<List<ApiVideo>>(emptyList())
    val videos: StateFlow<List<ApiVideo>> = _videos

    fun fetchReviews(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = TmdbClient.api.getReviews(movieId, Secrets.API_KEY)
                _reviews.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchVideos(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = TmdbClient.api.getVideos(movieId, Secrets.API_KEY)
                _videos.value = response.results.filter { it.site == "YouTube" && it.type == "Trailer" }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}