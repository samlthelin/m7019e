package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.laboration1.model.Movie
import com.example.laboration1.ui.component.GenreChips
import com.example.laboration1.ui.component.TopBarWithHome

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
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            GenreChips(genres = movie.genres)
            Spacer(modifier = Modifier.Companion.padding(8.dp))

            ClickableText(
                text = AnnotatedString("Visit Homepage"),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage))
                    context.startActivity(intent)
                },
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

            Button(onClick = { navController.navigate("third") }) {
                Text("Go to Third Screen")
            }

        }
    }
}