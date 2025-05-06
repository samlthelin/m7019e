// ui/viewmodel/MovieViewModel.kt
package com.example.laboration1.ui.viewmodel


import com.example.laboration1.data.MovieRepository      // ← correct package
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboration1.core.connectivity.ConnectivityObserver
import com.example.laboration1.di.ServiceLocator
import com.example.laboration1.model.Movie
import com.example.laboration1.network.TmdbClient
import com.example.laboration1.network.model.ApiReview
import com.example.laboration1.network.model.ApiVideo
import com.example.laboration1.url.Secrets
import com.example.laboration1.core.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    /* -------------------------------------------------------
     *  DEPENDENCIES  (enkel Service‑locator, byt gärna till Hilt)
     * ------------------------------------------------------ */
    private val repo: MovieRepository = ServiceLocator.provideRepository(application)
    private val connectivity: ConnectivityObserver = ServiceLocator.provideConnectivity(application)

    /* -------------------------------------------------------
     *  LIST ‑ FLÖDE
     * ------------------------------------------------------ */
    private val _category = MutableStateFlow("popular")         // eller "top_rated" osv.

    val movies: StateFlow<Resource<List<Movie>>> =
        _category.flatMapLatest { cat -> repo.movies(cat) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)

    /** Praktiskt om du vill fortsätta visa per genre som i din nuvarande HomeScreen2. */
    val genreSections: StateFlow<Map<String, List<Movie>>> =
        movies.map { res ->
            if (res is Resource.Success) {
                res.data
                    .flatMap { m -> m.genres.map { it to m } }
                    .groupBy({ it.first }, { it.second })
            } else emptyMap()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    fun selectCategory(cat: String) {
        _category.value = cat
    }

    /** Triggar en ny körning av networkBoundResource. */
    fun refresh() {
        _category.value = _category.value
    }

    /* Auto‑refresh när nätet kommer tillbaka */
    init {
        connectivity.status
            .onEach { online -> if (online) refresh() }
            .launchIn(viewModelScope)
    }

    /* -------------------------------------------------------
     *  DETAIL SCREEN
     * ------------------------------------------------------ */
    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    fun fetchMovieDetails(movieId: Int) = viewModelScope.launch {
        try {
            val res = TmdbClient.api.getMovieDetails(movieId, Secrets.API_KEY)
            _selectedMovie.value = Movie(
                id = res.id,
                title = res.title,
                genres = res.genres.map { it.name },
                homepage = res.homepage,
                imdbId = res.imdbId,
                posterPath = res.posterPath
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* -------------------------------------------------------
     *  THIRD SCREEN (reviews + trailers)
     * ------------------------------------------------------ */
    private val _reviews = MutableStateFlow<List<ApiReview>>(emptyList())
    val reviews: StateFlow<List<ApiReview>> = _reviews

    private val _videos = MutableStateFlow<List<ApiVideo>>(emptyList())
    val videos: StateFlow<List<ApiVideo>> = _videos

    fun fetchReviews(movieId: Int) = viewModelScope.launch {
        try {
            val res = TmdbClient.api.getReviews(movieId, Secrets.API_KEY)
            _reviews.value = res.results
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchVideos(movieId: Int) = viewModelScope.launch {
        try {
            val res = TmdbClient.api.getVideos(movieId, Secrets.API_KEY)
            _videos.value = res.results.filter { it.site == "YouTube" && it.type == "Trailer" }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
