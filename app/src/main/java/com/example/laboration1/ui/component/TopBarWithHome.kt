package com.example.laboration1.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

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