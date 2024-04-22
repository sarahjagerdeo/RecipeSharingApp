package com.example.project2

import com.google.firebase.database.IgnoreExtraProperties

import java.io.Serializable

data class UserGeneratedRecipe(
    val title: String,
    val ingredients: String,
    val instructions: String,
    val tags: String
) : Serializable

