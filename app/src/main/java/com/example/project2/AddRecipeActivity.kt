package com.example.project2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        database = FirebaseDatabase.getInstance().reference.child("recipes")

        val titleField = findViewById<EditText>(R.id.editTextTitle)
        val ingredientsField = findViewById<EditText>(R.id.editTextIngredients)
        val instructionsField = findViewById<EditText>(R.id.editTextInstructions)


        findViewById<Button>(R.id.buttonSubmitRecipe).setOnClickListener {
            val title = titleField.text.toString().trim()
            val ingredients = ingredientsField.text.toString().trim()
            val instructions = instructionsField.text.toString().trim()

            if (title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            submitRecipe(title, ingredients, instructions)
        }
    }

    private fun submitRecipe(title: String, ingredients: String, instructions: String) {
        val recipeData = hashMapOf(
            "title" to title,
            "ingredients" to ingredients,
            "instructions" to instructions,
        )

        database.push().setValue(recipeData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Recipe submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error submitting recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
