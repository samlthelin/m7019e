package com.example.laboration1.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.laboration1.network.ConnectionStatus
import com.example.laboration1.network.ConnectivityObserver
import com.example.laboration1.network.model.ApiVideo
import com.example.laboration1.ui.component.ReviewCard
import com.example.laboration1.ui.component.TopBarWithHome
import com.example.laboration1.ui.component.VideoCard
import com.example.laboration1.ui.viewmodel.MovieViewModel
import com.example.laboration1.ui.viewmodel.MovieViewModelFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(navController: NavController, movieId: Int) {
    val context = LocalContext.current
    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(context))
    val reviews = viewModel.reviews.collectAsState().value
    val videos = viewModel.videos.collectAsState().value
    val connectivityObserver = remember { ConnectivityObserver(context) }

    LaunchedEffect(movieId) {
        viewModel.fetchReviews(movieId)
        viewModel.fetchVideos(movieId)

        connectivityObserver.connectionStatus.collect { status ->
            if (status == ConnectionStatus.Available) {
                Log.d("ThirdScreen", "Connection restored: re-fetching extras!")
                viewModel.fetchReviews(movieId)
                viewModel.fetchVideos(movieId)
            }
        }
    }

    val sampleVideo = ApiVideo(
        id = "sample",
        key = "sample_key",
        name = "Sample Trailer",
        site = "Sample",
        type = "Trailer"
    )

    val allVideos = listOf(sampleVideo) + videos

    Scaffold(
        topBar = { TopBarWithHome(title = "Extras", navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews) { review ->
                        ReviewCard(review)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(
                    "Trailers",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
                    items(allVideos) { video ->
                        VideoCard(video)
                    }
                }
            }
        }
    }
}
