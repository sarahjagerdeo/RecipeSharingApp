package com.example.project2

import java.io.Serializable

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val description: String,
    val rating: Float,
): Serializable