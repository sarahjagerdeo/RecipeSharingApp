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


class RecipeDetailsActivity : AppCompatActivity() {

    private lateinit var recipeTitleTextView: TextView
    private lateinit var instructionsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeTitleTextView = findViewById(R.id.recipeTitleTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)

        val recipe = intent.getSerializableExtra("recipe") as? Recipe

        recipe?.let {
            recipeTitleTextView.text = it.title
            fetchAnalyzedRecipeInstructions(it.id)
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
                println("JSON Response: $responseData")
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

    private fun parseAnalyzedInstructionsResponse(jsonData: String?): List<String> {
        val instructions = mutableListOf<String>()

        jsonData?.let { jsonString ->
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val stepObject = jsonArray.getJSONObject(i)
                val step = stepObject.getString("step")
                instructions.add(step)
                Log.d("Parsing", "Step $i: $step")
            }
        }

        return instructions
    }

    private fun displayRecipeInstructions(instructions: List<String>) {
        // Display recipe instructions in the UI
        val instructionsText = instructions.joinToString(separator = "\n")
        instructionsTextView.text = instructionsText
    }
}
