package com.example.project2

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class RecipeDetailsActivity : AppCompatActivity() {

    private lateinit var recipeTitleTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var ingredientsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeTitleTextView = findViewById(R.id.recipeTitleTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)

        val recipe = intent.getSerializableExtra("recipe") as? Recipe

        recipe?.let {
            recipeTitleTextView.text = it.title
            fetchAnalyzedRecipeInstructions(it.id)
            fetchRecipeIngredients(it.id) // Fetch the ingredients
        }
    }

    private fun fetchAnalyzedRecipeInstructions(recipeId: Int) {
        val apiKey = "a99b580ce71041ae8fa0be722da7c276"
        val url =
            "https://api.spoonacular.com/recipes/$recipeId/analyzedInstructions?apiKey=$apiKey"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                val instructions = parseAnalyzedInstructionsResponse(responseData)
                withContext(Dispatchers.Main) {
                    displayRecipeInstructions(instructions)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.e("RecipeDetailsActivity", "Error fetching recipe details", e)
                }
            }
        }
    }

    private fun fetchRecipeIngredients(recipeId: Int) {
        val apiKey = "a99b580ce71041ae8fa0be722da7c276"
        val url = "https://api.spoonacular.com/recipes/$recipeId/ingredientWidget.json?apiKey=$apiKey"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                val ingredients = parseRecipeIngredientsResponse(responseData)
                withContext(Dispatchers.Main) {
                    displayRecipeIngredients(ingredients)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.e("RecipeDetailsActivity", "Error fetching recipe ingredients", e)
                }
            }
        }
    }

    private fun parseAnalyzedInstructionsResponse(jsonData: String?): List<String> {
        val instructions = mutableListOf<String>()

        jsonData?.let { jsonString ->
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val instructionObject = jsonArray.getJSONObject(i)

                if (instructionObject.has("steps")) {
                    val stepsArray = instructionObject.getJSONArray("steps")

                    for (j in 0 until stepsArray.length()) {
                        val stepObject = stepsArray.getJSONObject(j)
                        if (stepObject.has("step")) {
                            val step = stepObject.getString("step")
                            instructions.add(step)
                        }
                    }
                }
            }
        }
        return instructions
    }

    private fun parseRecipeIngredientsResponse(jsonData: String?): List<String> {
        val ingredients = mutableListOf<String>()

        jsonData?.let { jsonString ->
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.has("ingredients")) {
                val ingredientsArray = jsonObject.getJSONArray("ingredients")

                for (i in 0 until ingredientsArray.length()) {
                    val ingredientObject = ingredientsArray.getJSONObject(i)
                    if (ingredientObject.has("name")) {
                        val ingredient = ingredientObject.getString("name")
                        ingredients.add(ingredient)
                    }
                }
            }
        }

        return ingredients
    }

    private fun displayRecipeInstructions(instructions: List<String>) {
        val instructionsText = instructions.joinToString(separator = "\n")
        instructionsTextView.text = instructionsText
    }

    private fun displayRecipeIngredients(ingredients: List<String>) {
        val ingredientsText = ingredients.joinToString(separator = "\n")
        ingredientsTextView.text = ingredientsText
    }
}
