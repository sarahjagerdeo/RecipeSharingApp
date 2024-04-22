package com.example.project2

import com.google.firebase.database.IgnoreExtraProperties

import java.io.Serializable

@IgnoreExtraProperties
data class UserGeneratedRecipe(
    var ingredients: String = "",
    var instructions: String = "",
    var tags: String = "",
    var title: String = ""
) : Serializable {
    // No-argument constructor
    constructor() : this("", "", "", "")
}

