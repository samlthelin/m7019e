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



// just extend the viewmodel android class!!
class MovieViewModel : ViewModel() {

    // mutable list. stateflow since we want this to listen to UI cahnges.
    // remember: mutablestateflow is mutable. do not let UI see this!
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    private val _genreSections = MutableStateFlow<Map<String, List<Movie>>>(emptyMap())
    val genreSections: StateFlow<Map<String, List<Movie>>> = _genreSections

    // fetch the popular movies when we start (look at fetchMovies..)
    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        // important since it is running in background. when viewmodel is destroyed we want these to cancel as well
        viewModelScope.launch {
            try {

                // viewmodel handles the retro api fetch. fetch genres and popular movies:
                val genreResponse = TmdbClient.api.getGenres(Secrets.API_KEY)
                val genreMap = genreResponse.genres.associateBy({ it.id }, { it.name }) // since API we need to bind id to the name of each movie.

                val movieResponse = TmdbClient.api.getPopularMovies(Secrets.API_KEY)
                val movieList = movieResponse.results.map { it.toMovie(genreMap) } // bind movie to genre

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


    //---- detail screen ----
    // when a user taps a movie we need extra details for detail screen. handled here!
    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    fun fetchMovieDetails(movieId: Int) {
        // important since it is running in background. when viewmodel is destroyed we want these to cancel as well
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


    // ---- third screen ----
    // store results in stateflows so the screen can actually react....
    private val _reviews = MutableStateFlow<List<ApiReview>>(emptyList())
    val reviews: StateFlow<List<ApiReview>> = _reviews

    private val _videos = MutableStateFlow<List<ApiVideo>>(emptyList())
    val videos: StateFlow<List<ApiVideo>> = _videos

    // use defined endpoints..!
    fun fetchReviews(movieId: Int) {
        // important since it is running in background. when viewmodel is destroyed we want these to cancel as well
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
        // important since it is running in background. when viewmodel is destroyed we want these to cancel as well
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