package com.example.project2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.ImageView
import android.widget.RatingBar
//import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = listOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.recipeTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.recipeDescriptionTextView)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.recipeRatingBar)
        private val imageView: ImageView = itemView.findViewById(R.id.recipeImageView)

        fun bind(recipe: Recipe) {
            titleTextView.text = recipe.title
            descriptionTextView.text = recipe.description
            ratingBar.rating = recipe.rating

            Glide.with(itemView)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)
        }
    }

}
