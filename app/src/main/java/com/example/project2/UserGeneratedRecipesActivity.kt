package com.example.project2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class UserGeneratedRecipesActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var userGeneratedRecipeAdapter: UserGeneratedRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_generated_recipes)

        database = FirebaseDatabase.getInstance().reference.child("recipes")

        val recyclerView = findViewById<RecyclerView>(R.id.userGeneratedRecipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userGeneratedRecipeAdapter = UserGeneratedRecipeAdapter()
        recyclerView.adapter = userGeneratedRecipeAdapter

        fetchUserGeneratedRecipes()
    }

    private fun fetchUserGeneratedRecipes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userGeneratedRecipes = mutableListOf<UserGeneratedRecipe>()
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.value
                    if (data is Map<*, *>) {
                        val expectedKeys = setOf("ingredients", "instructions", "title")
                        val hasAllKeys = expectedKeys.all { key -> data.containsKey(key) && data[key] is String }
                        if (hasAllKeys) {
                            val userGeneratedRecipe = snapshot.getValue(UserGeneratedRecipe::class.java)
                            if (userGeneratedRecipe != null) {
                                userGeneratedRecipes.add(userGeneratedRecipe)
                            }
                        }
                    }
                }
                userGeneratedRecipeAdapter.setUserGeneratedRecipes(userGeneratedRecipes)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
