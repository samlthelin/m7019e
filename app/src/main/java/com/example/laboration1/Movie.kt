package com.example.laboration1

data class Movie(
    val id: Int,
    val title: String,
    val genres: List<String>,
    val homepage: String,
    val imdbId: String
)

object MovieRepository {
    val movieList = listOf(
        Movie(
            id = 550,
            title = "Fight Club",
            genres = listOf("Drama"),
            homepage = "http://www.foxmovies.com/movies/fight-club",
            imdbId = "tt0137523"
        ),
        Movie(
            id = 155,
            title = "The Dark Knight",
            genres = listOf("Action", "Crime", "Drama"),
            homepage = "http://thedarkknight.warnerbros.com/",
            imdbId = "tt0468569"
        ),
        Movie(
            id = 278,
            title = "The Shawshank Redemption",
            genres = listOf("Drama", "Crime"),
            homepage = "https://www.warnerbros.com/movies/shawshank-redemption",
            imdbId = "tt0111161"
        ),
        Movie(
            id = 157336,
            title = "Interstellar",
            genres = listOf("Adventure", "Drama", "Sci-Fi"),
            homepage = "https://www.interstellarmovie.net/",
            imdbId = "tt0816692"
        ),
        Movie(
            id = 603,
            title = "The Matrix",
            genres = listOf("Action", "Sci-Fi"),
            homepage = "https://www.warnerbros.com/movies/matrix",
            imdbId = "tt0133093"
        )
    )
}