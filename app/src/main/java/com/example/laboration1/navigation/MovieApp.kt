package com.example.laboration1.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.laboration1.data.MovieRepository
import com.example.laboration1.ui.screen.DetailScreen
import com.example.laboration1.ui.screen.HomeScreen
import com.example.laboration1.ui.screen.HomeScreen2
import com.example.laboration1.ui.screen.ThirdScreen

@Composable
fun MovieApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen2(navController)
        }
        composable("details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (movieId != null) {
                DetailScreen(navController, movieId)
            } else {
                Text("Movie not found")
            }
        }
        composable("third/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (movieId != null) {
                ThirdScreen(navController, movieId)
            } else {
                Text("No movie selected")
            }
        }
    }
}
