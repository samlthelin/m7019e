package com.example.laboration1.ui.component

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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