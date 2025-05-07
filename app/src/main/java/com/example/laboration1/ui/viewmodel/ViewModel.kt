package com.example.laboration1.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboration1.data.repository.MovieRepository
import com.example.laboration1.model.Movie
import com.example.laboration1.network.model.ApiReview
import com.example.laboration1.network.model.ApiVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(context: Context) : ViewModel() {

    private val repository = MovieRepository(context)

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _genreSections = MutableStateFlow<Map<String, List<Movie>>>(emptyMap())
    val genreSections: StateFlow<Map<String, List<Movie>>> = _genreSections

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    private val _reviews = MutableStateFlow<List<ApiReview>>(emptyList())
    val reviews: StateFlow<List<ApiReview>> = _reviews

    private val _videos = MutableStateFlow<List<ApiVideo>>(emptyList())
    val videos: StateFlow<List<ApiVideo>> = _videos

    fun fetchMovies(viewType: String) {
        viewModelScope.launch {
            repository.fetchMovies(viewType)
                .onSuccess { (movieList, grouped) ->
                    _movies.value = movieList
                    _genreSections.value = grouped
                }
                .onFailure {
                    Log.e("MovieViewModel", "Failed to fetch movies: ${it.message}")
                }
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            repository.fetchMovieDetails(movieId)
                .onSuccess { movie ->
                    _selectedMovie.value = movie
                }
                .onFailure {
                    Log.e("MovieViewModel", "Failed to fetch movie details: ${it.message}")
                }
        }
    }

    fun fetchReviews(movieId: Int) {
        viewModelScope.launch {
            repository.fetchReviews(movieId)
                .onSuccess { _reviews.value = it }
                .onFailure {
                    Log.e("MovieViewModel", "Failed to fetch reviews: ${it.message}")
                }
        }
    }

    fun fetchVideos(movieId: Int) {
        viewModelScope.launch {
            repository.fetchVideos(movieId)
                .onSuccess { _videos.value = it }
                .onFailure {
                    Log.e("MovieViewModel", "Failed to fetch videos: ${it.message}")
                }
        }
    }

    fun scheduleMovieCaching(sortType: String) {
        repository.scheduleMovieCaching(sortType)
    }
}
