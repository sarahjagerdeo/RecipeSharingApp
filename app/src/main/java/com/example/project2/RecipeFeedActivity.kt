package com.example.project2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class RecipeFeedActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var querySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_feed)

        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        querySpinner = findViewById(R.id.querySpinner)

        adapter = RecipeAdapter()
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.adapter = adapter
        val queries = listOf("Pasta", "Rice", "Pizza", "Salad", "Chicken", "Fish", "Steak", "Tofu", "Sugar", "Flour")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, queries)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        querySpinner.adapter = spinnerAdapter
        querySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedQuery = queries[position]
                fetchRecipes(selectedQuery)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        fetchRecipes(queries.first())
    }

    private fun fetchRecipes(query: String) {
        val apiKey = "a99b580ce71041ae8fa0be722da7c276"
        val url = "https://api.spoonacular.com/recipes/complexSearch?query=$query&apiKey=$apiKey"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                val recipes = parseJsonResponse(responseData)
                withContext(Dispatchers.Main) {
                    adapter.setRecipes(recipes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RecipeFeedActivity, "Error fetching recipes", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseJsonResponse(jsonData: String?): List<Recipe> {
        val recipes = mutableListOf<Recipe>()

        jsonData?.let { jsonString ->
            val jsonObject = JSONObject(jsonString)
            val resultsArray = jsonObject.getJSONArray("results")
            for (i in 0 until resultsArray.length()) {
                val recipeObject = resultsArray.getJSONObject(i)
                val id = recipeObject.getInt("id")
                val title = recipeObject.getString("title")
                val imageUrl = recipeObject.getString("image")
                val rating = recipeObject.optDouble("rating", 0.0).toFloat()
                val recipe = Recipe(id, title, imageUrl, "", rating)
                recipes.add(recipe)
            }
        }

        return recipes
    }
}
