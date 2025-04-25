package com.example.laboration1.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    val results: List<ApiVideo>
)

@Serializable
data class ApiVideo(
    val id: String,
    val key: String, // This is the YouTube video key
    val name: String,
    val site: String,
    val type: String
)
