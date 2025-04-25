package com.example.laboration1.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val results: List<ApiReview>
)

@Serializable
data class ApiReview(
    val author: String,
    val content: String
)
