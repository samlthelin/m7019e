package com.example.laboration1.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.laboration1.Workers.CacheMoviesWorker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithSort(currentSort: String, onSortChange: (String)-> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val sortOptions = listOf("popular", "top_rated")
    val context = LocalContext.current
    TopAppBar(
        title = { Text("Movies by Genre") },
        actions = {
            IconButton(onClick = {
                val workRequest = OneTimeWorkRequestBuilder<CacheMoviesWorker>()
                    .setInputData(workDataOf("sortType" to "popular"))
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)
            }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Cache Popular Movies"
                )
            }
            Box {
                Text(
                    text = currentSort.replace("_", " ").replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable { expanded = true }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                onSortChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    )

}