package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue          //  ← NEW
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.laboration1.core.util.Resource
import com.example.laboration1.model.Movie      //  ← make sure this is your model
import com.example.laboration1.ui.component.GenreSection
import com.example.laboration1.ui.component.LoadingScreen
import com.example.laboration1.ui.component.NoConnectionScreen
import com.example.laboration1.ui.viewmodel.MovieViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(
    navController: NavController,
    viewModel: MovieViewModel = viewModel()
) {
    /* 1 — collect StateFlow without initial argument */
    val state by viewModel.movies.collectAsStateWithLifecycle()    // NEW

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movies by Genre") }) }
    ) { paddingValues ->

        /* 2 — use wildcard (<*>) for the generic sub‑classes */
        when (state) {

            is Resource.Loading -> LoadingScreen()

            is Resource.Error<*> -> NoConnectionScreen(
                onRetry = { viewModel.refresh() }
            )

            is Resource.Success<*> -> {
                val movies = (state as Resource.Success<List<Movie>>).data

                val genreSections = movies
                    .flatMap { m -> m.genres.map { it to m } }
                    .groupBy({ it.first }, { it.second })

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    genreSections.forEach { (genre, list) ->
                        item {
                            GenreSection(
                                genre = genre,
                                movies = list,
                                onMovieClick = {
                                    navController.navigate("details/${it.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}