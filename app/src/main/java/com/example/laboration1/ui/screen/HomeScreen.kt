package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.laboration1.data.MovieRepository
import com.example.laboration1.model.Movie

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val movies = MovieRepository.movieList

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Movie List") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieItem(movie = movie) {
                    navController.navigate("details/${movie.id}")

                }
                Spacer(modifier = Modifier.Companion.padding(4.dp))
            }
        }

    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier

            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),

        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movie.title.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

        }
    }
}