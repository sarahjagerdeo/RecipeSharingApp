package com.example.project2

import com.google.firebase.database.IgnoreExtraProperties

import java.io.Serializable

@IgnoreExtraProperties
data class UserGeneratedRecipe(
    var id: String = "",
    var ingredients: String = "",
    var instructions: String = "",
    var tags: String = "",
    var title: String = ""
) : Serializable {

    constructor() : this("", "", "", "")
}

