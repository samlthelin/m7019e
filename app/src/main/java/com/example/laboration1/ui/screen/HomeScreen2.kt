package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.laboration1.data.MovieRepository
import com.example.laboration1.model.Movie
import com.example.laboration1.ui.component.GenreSection
import com.example.laboration1.url.Constants
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laboration1.ui.viewmodel.MovieViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(navController: NavController) {
    val viewModel: MovieViewModel = viewModel()
    //val movies = viewModel.movies.collectAsState().value
    val genreSections = viewModel.genreSections.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Movies by Genre") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(genreSections.toList()) { (genre, moviesInGenre) ->
                GenreSection(
                    genre = genre,
                    movies = moviesInGenre,
                    onMovieClick = { movie ->
                        navController.navigate("details/${movie.id}")
                    }
                )
            }
        }

    }
}


@Composable
fun MovieItem2(movie: Movie, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .width(150.dp)
            .height(225.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        AsyncImage(
            model = "${Constants.POSTER_IMAGE_BASE_URL}${Constants.POSTER_IMAGE_BASE_WIDTH}${movie.posterPath}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
