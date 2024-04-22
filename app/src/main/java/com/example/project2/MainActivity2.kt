package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Find the button to navigate to the recipe feed
        val recipeFeedButton: Button = findViewById(R.id.recipeFeedButton)
        recipeFeedButton.setOnClickListener {
            val intent = Intent(this, RecipeFeedActivity::class.java)
            startActivity(intent)
        }

        // Find the button to navigate to the add recipe activity
        val uploadRecipeButton: Button = findViewById(R.id.uploadRecipeButton)
        uploadRecipeButton.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        val viewRecipesButton: Button = findViewById(R.id.viewUploadedRecipesButton)
        viewRecipesButton.setOnClickListener {
            val intent = Intent(this, UserGeneratedRecipesActivity::class.java)
            startActivity(intent)
        }
    }
}
