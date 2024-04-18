package com.example.project2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project2.Recipe
import com.example.project2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class RecipeDetailsActivity : AppCompatActivity() {

    private lateinit var recipeTitleTextView: TextView
    private lateinit var ingredientsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeTitleTextView = findViewById(R.id.recipeTitleTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)

        val recipe = intent.getSerializableExtra("recipe") as? Recipe

        recipe?.let {
            recipeTitleTextView.text = it.title
            fetchRecipeDetails(it.id)
        }
    }

    private fun fetchRecipeDetails(recipeId: Int) {
        val apiKey = "a99b580ce71041ae8fa0be722da7c276"
        val url = "https://api.spoonacular.com/recipes/$recipeId/information?includeNutrition=true&apiKey=$apiKey"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                val details = parseJsonResponse(responseData)
                withContext(Dispatchers.Main) {
                    displayRecipeDetails(details)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // Handle error
                }
            }
        }
    }

    private fun parseJsonResponse(jsonData: String?): RecipeDetails {
        val ingredients = mutableListOf<String>()
        val nutrition = mutableMapOf<String, String>()

        jsonData?.let { jsonString ->
            val jsonObject = JSONObject(jsonString)

            // Extract ingredients
            val ingredientsArray = jsonObject.getJSONArray("extendedIngredients")
            for (i in 0 until ingredientsArray.length()) {
                val ingredientObject = ingredientsArray.getJSONObject(i)
                val ingredientName = ingredientObject.getString("name")
                ingredients.add(ingredientName)
            }

            // Extract nutrition data if available
            val nutritionObject = jsonObject.optJSONObject("nutrition")
            nutritionObject?.let {
                // Assuming nutrition data is represented as key-value pairs in the JSON
                // Adjust the keys as per the actual JSON structure
                nutrition["Calories"] = it.getString("calories")
                nutrition["Protein"] = it.getString("protein")

            }
        }

        return RecipeDetails(ingredients, nutrition)
    }


    private fun displayRecipeDetails(details: RecipeDetails) {
        // Display recipe details in the UI
        val ingredientsText = details.ingredients.joinToString(separator = "\n")
        ingredientsTextView.text = ingredientsText
    }
}
