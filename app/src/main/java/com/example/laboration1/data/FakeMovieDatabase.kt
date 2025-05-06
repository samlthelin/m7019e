package com.example.laboration1.data
import com.example.laboration1.model.Movie


object FakeMovieDatabase {
    val movieList = listOf(
        Movie(
            id = 550,
            title = "Fight Club",
            genres = listOf("Drama"),
            homepage = "http://www.foxmovies.com/movies/fight-club",
            imdbId = "tt0137523",
            posterPath = "/a26cQPRhJPX6GbWfQbvZdrrp9j9.jpg"
        ),
        Movie(
            id = 155,
            title = "The Dark Knight",
            genres = listOf("Action", "Crime", "Drama"),
            homepage = "http://thedarkknight.warnerbros.com/",
            imdbId = "tt0468569",
            posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg"
        ),
        Movie(
            id = 278,
            title = "The Shawshank Redemption",
            genres = listOf("Drama", "Crime"),
            homepage = "https://www.warnerbros.com/movies/shawshank-redemption",
            imdbId = "tt0111161",
            posterPath = "/lyQBXzOQSuE59IsHyhrp0qIiPAz.jpg"
        ),
        Movie(
            id = 157336,
            title = "Interstellar",
            genres = listOf("Adventure", "Drama", "Sci-Fi"),
            homepage = "https://www.interstellarmovie.net/",
            imdbId = "tt0816692",
            posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
        ),
        Movie(
            id = 603,
            title = "The Matrix",
            genres = listOf("Action", "Sci-Fi"),
            homepage = "https://www.warnerbros.com/movies/matrix",
            imdbId = "tt0133093",
            posterPath = "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"
        )
    )
}