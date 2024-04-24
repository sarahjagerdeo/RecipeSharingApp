package com.example.project2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserGeneratedRecipeAdapter : RecyclerView.Adapter<UserGeneratedRecipeAdapter.ViewHolder>() {

    private var userGeneratedRecipes = listOf<UserGeneratedRecipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_generated_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userGeneratedRecipe = userGeneratedRecipes[position]
        holder.bind(userGeneratedRecipe)
    }

    override fun getItemCount(): Int {
        return userGeneratedRecipes.size
    }

    fun setUserGeneratedRecipes(recipes: List<UserGeneratedRecipe>) {
        this.userGeneratedRecipes = recipes
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.userGeneratedRecipeTitleTextView)
        private val ingredientsTextView: TextView = itemView.findViewById(R.id.userGeneratedRecipeIngredientsTextView)
        private val instructionsTextView: TextView = itemView.findViewById(R.id.userGeneratedRecipeInstructionsTextView)

        fun bind(userGeneratedRecipe: UserGeneratedRecipe) {
            titleTextView.text = userGeneratedRecipe.title
            ingredientsTextView.text = userGeneratedRecipe.ingredients
            instructionsTextView.text = userGeneratedRecipe.instructions
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val userGeneratedRecipe = userGeneratedRecipes[position]
                    val intent = Intent(itemView.context, UserGeneratedRecipesActivity::class.java)
                    intent.putExtra("userGeneratedRecipe", userGeneratedRecipe)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}
