package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.laboration1.model.Movie
import com.example.laboration1.ui.component.GenreSection
import com.example.laboration1.url.Constants
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laboration1.network.ConnectionStatus
import com.example.laboration1.network.ConnectivityObserver
import com.example.laboration1.ui.component.TopBarWithSort
import com.example.laboration1.ui.viewmodel.MovieViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(navController: NavController) {
    // use viewmodel to fetch, manage states and expose flows to UI
    val viewModel: MovieViewModel = viewModel()
    val context = LocalContext.current

    // mutable! always start as popular.
    var sortType by rememberSaveable { mutableStateOf("popular") }
    // remember for checking the network status.
    val connectivityObserver = remember { ConnectivityObserver(context) }

    // we use connectivity observer. essentially, if we launch without internet but get it later,
    // refresh the page and fetch movies from api!
    LaunchedEffect(Unit) {
        // if we launch the screen without connection it waits. once internet is back, we fetch automatically!
        connectivityObserver.connectionStatus.collect { status ->
            if (status == ConnectionStatus.Available) {
                viewModel.fetchMovies(sortType, context)
            }
        }
    }

    //launchedeffect on sorttype. observe when it changes and act when it does!
    LaunchedEffect(sortType) {
        Log.d("HomeScreen2","Fetching movies for type: ${sortType}")
        viewModel.fetchMovies(sortType,context)

        //might not be needed, but it works for ensuring the movies are cached if the app crashes
        // or loses power!!!
        viewModel.scheduleMovieCaching(context,sortType) // trigger background worker
    }

    val movies = viewModel.movies.collectAsState().value
    val genreSections = viewModel.genreSections.collectAsState().value

    LaunchedEffect(movies) {
        Log.d("HomeScreen2","WE RECEIVED ${movies.size} MOVIES")
    }

    Scaffold(
        topBar = {
            TopBarWithSort(currentSort = sortType, onSortChange = {sortType=it})
        }
    ) { paddingValues->
        if (genreSections.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...")
                }
            }
        } else {
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
