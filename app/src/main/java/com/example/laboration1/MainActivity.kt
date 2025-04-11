package com.example.laboration1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.laboration1.navigation.MovieApp

import com.example.laboration1.ui.theme.Laboration1Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboration1Theme {
                MovieApp()
            }
        }
    }
}


