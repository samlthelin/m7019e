package com.example.laboration1.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laboration1.model.Movie
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreChips(genres: List<String>) {
    Row(modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp)
    ) {
        genres.forEach { genre ->
            AssistChip(
                onClick = {}, // no-op
                label = { Text(genre) },
                modifier = Modifier.padding(end = 8.dp),
                colors = AssistChipDefaults.assistChipColors()
            )
        }
    }
}

@Composable
fun GenreSection(
    genre: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = genre,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        LazyRow {
            items(movies) { movie ->
                MovieItem2(
                    movie = movie,
                    onClick = { /* inget */ }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

        }
    }
}
