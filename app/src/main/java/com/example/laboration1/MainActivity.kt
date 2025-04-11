package com.example.laboration1

import androidx.compose.foundation.lazy.items
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.laboration1.Movie
import com.example.laboration1.MovieRepository



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieApp()
        }
    }
}


@Composable
fun MovieApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            val movie = MovieRepository.movieList.find { it.id == movieId }

            if (movie != null) {
                DetailScreen(navController, movie)
            } else {
                Text("Movie not found.")
            }
        }
        composable("third") {
            ThirdScreen(navController)
        }
    }
}
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieItem(movie = movie) {
                    navController.navigate("details/${movie.id}")
                }
                Spacer(modifier = Modifier.padding(4.dp))
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, movie: Movie) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarWithHome(title = movie.title, navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GenreChips(genres = movie.genres)
            Spacer(modifier = Modifier.padding(8.dp))

            ClickableText(
                text = AnnotatedString("Visit Homepage"),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage))
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(8.dp)
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

            Button(onClick = { navController.navigate("third") }) {
                Text("Go to Third Screen")
            }

        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBarWithHome(title = "Third Screen", navController = navController)
        }
    )
    {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("This is the third screen.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithHome(title: String, navController: NavController) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true } // Prevents backstack piling
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Go to Home"
                )
            }
        }
    )
}


