package com.example.laboration1.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboration1.data.MovieRepository.movieList
import com.example.laboration1.model.Movie
import com.example.laboration1.network.TmdbClient
import com.example.laboration1.network.model.ApiReview
import com.example.laboration1.network.model.ApiVideo
import com.example.laboration1.data.toMovie
import com.example.laboration1.data.toDetailEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.laboration1.url.Secrets
import com.example.laboration1.data.local.MovieDatabase
import com.example.laboration1.data.toEntity
import com.example.laboration1.network.model.MovieResponse
import com.example.laboration1.network.model.ReviewResponse
import com.example.laboration1.network.model.VideoResponse



// just extend the viewmodel android class!!
class MovieViewModel : ViewModel() {

    // mutable list. stateflow since we want this to listen to UI cahnges.
    // remember: mutablestateflow is mutable. do not let UI see this!
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _genreSections = MutableStateFlow<Map<String, List<Movie>>>(emptyMap())
    val genreSections: StateFlow<Map<String, List<Movie>>> = _genreSections

    // fetch the popular movies when we start (look at fetchMovies..)
    //init {
    //    fetchMovies()
    //}

    fun fetchMovies(viewType: String, context: Context) {
        // Important since it is running in background. When viewmodel is destroyed we want these to cancel as well
        viewModelScope.launch {
            val db = MovieDatabase.getDatabase(context)
            val dao = db.movieDao()

            try {
                // 1. Load cached first, before calling API (offline support!)
                val cached = dao.getMoviesByViewType(viewType).map { it.toMovie() }
                Log.d("Room Cache", "Loaded ${cached.size} cached movies for viewType = $viewType")
                _movies.value = cached

                val groupedCached = cached
                    .flatMap { movie -> movie.genres.map { genre -> genre to movie } }
                    .groupBy({ it.first }, { it.second })
                _genreSections.value = groupedCached

                // 2. Fetch genres and either popular or top-rated
                val genreResponse = TmdbClient.api.getGenres(Secrets.API_KEY)
                val genreMap = genreResponse.genres.associateBy({ it.id }, { it.name }) // used to map ids to names

                val movieResponse = when (viewType) {
                    "top_rated" -> TmdbClient.api.getTopRatedMovies(Secrets.API_KEY)
                    else -> TmdbClient.api.getPopularMovies(Secrets.API_KEY)
                }

                val movieList = movieResponse.results.map { it.toMovie(genreMap) }
                Log.d("MovieViewModel", "Fetched ${movieList.size} movies")

                _movies.value = movieList

                // 3. Update genre sections
                val grouped = movieList
                    .flatMap { movie -> movie.genres.map { genre -> genre to movie } }
                    .groupBy({ it.first }, { it.second })

                _genreSections.value = grouped




                // 4. Save to Room (after setting _movies, so it's snappy)
                val entities = movieResponse.results.map { it.toEntity(viewType, genreMap) }
                Log.d("EntityMapping", "Mapped ${entities.size} movies to entities")
                Log.d("Room Insert", "Saving ${entities.size} movies to ROom for viewType=$viewType")
                dao.deleteMoviesByViewType(viewType)
                dao.insertMovies(entities)
                Log.d("Room Insert", "iNSERTED ${entities.size} movies into Room for viewtype=$viewType")
            } catch (e: Exception) {
                Log.d("Error while fetching movies","Failed to fetch.")
                e.printStackTrace()

            }
        }
    }



    //---- detail screen ----
    // when a user taps a movie we need extra details for detail screen. handled here!
    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    fun fetchMovieDetails(movieId: Int, context: Context) {
        // important since it is running in background. when viewmodel is destroyed we want these to cancel as well
        viewModelScope.launch {
            val db = MovieDatabase.getDatabase(context)
            val dao = db.movieDao()

            val cached = dao.getMovieDetailById(movieId)
            if (cached != null){
                _selectedMovie.value = cached.toMovie()
            }

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
                _selectedMovie.value?.let { movie ->
                    dao.insertMovieDetail(movie.toDetailEntity())
                }

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