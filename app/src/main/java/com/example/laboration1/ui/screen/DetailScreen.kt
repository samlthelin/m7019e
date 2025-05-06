package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.example.laboration1.model.Movie
import com.example.laboration1.ui.component.GenreChips
import com.example.laboration1.ui.component.TopBarWithHome
import com.example.laboration1.url.Constants
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.laboration1.network.ConnectionStatus
import com.example.laboration1.network.ConnectivityObserver
import com.example.laboration1.ui.viewmodel.MovieViewModel
import java.time.format.TextStyle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, movieId: Int, viewModel: MovieViewModel = viewModel()) {
    val context = LocalContext.current
    val movie = viewModel.selectedMovie.collectAsState().value
    val connectivityObserver = remember { ConnectivityObserver(context) }

    LaunchedEffect(Unit) {
        connectivityObserver.connectionStatus.collect { status ->
            if (status == ConnectionStatus.Available && movie==null) {
                Log.d("DetailScreen","Connection to detailscreen restored: fetching movie!!!")
                viewModel.fetchMovieDetails(movieId,context)
            }
        }
    }

    // we're telling compose to only run this ONCE (on first composition).
    // without this, the screen could recompose 20 times and need to fetch the movies 20 times!
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId, context)
    }
    // makes it so that we do not need the safe calls (?)
    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }

    Scaffold(
        topBar = {
            TopBarWithHome(title = movie.title, navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            AsyncImage(
                model = "${Constants.POSTER_IMAGE_BASE_URL}${Constants.POSTER_IMAGE_BASE_WIDTH}${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(200.dp)
                    .height(300.dp)
                    .alpha(1f),
                    //.background(Color.Red),
                onSuccess = {
                    Log.d("Coil", "Poster loaded successfully!")
                },
                onError = { error ->
                    Log.e("Coil", "Failed to load poster: ${error.result.throwable.message}")
                }
            )

            Spacer(modifier = Modifier.Companion.padding(16.dp))
            GenreChips(genres = movie.genres)
            Spacer(modifier = Modifier.Companion.padding(8.dp))

            ClickableText(
                text = AnnotatedString("Visit Homepage"),

                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage))
                    context.startActivity(intent)
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.Companion.padding(8.dp)
            )

            Button(onClick = {
                //val context = LocalContext.current

                val imdbUri = Uri.parse("imdb:///title/${movie.imdbId}") // imdb app uri scheme
                val imdbIntent = Intent(Intent.ACTION_VIEW, imdbUri).apply {
                    setPackage("com.imdb.mobile") // essentially force open imdb app
                }

                try {
                    context.startActivity(imdbIntent)
                } catch (e: ActivityNotFoundException) {
                    // if app isn't installed, we go to the browser instead
                    val fallbackUri = Uri.parse("https://www.imdb.com/title/${movie.imdbId}")
                    val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
                    context.startActivity(fallbackIntent)
                }
            }) {
                Text("Open in IMDB App")
            }


            //Spacer(modifier = Modifier.padding(8.dp))

            Button(onClick = { navController.navigate("third/${movie.id}") }) {
                Text("Go to Third Screen")
            }


        }
    }
}