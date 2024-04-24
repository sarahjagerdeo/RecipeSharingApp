import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.UserGeneratedRecipe
import com.example.project2.UserGeneratedRecipesActivity
import com.google.firebase.database.FirebaseDatabase

class UserGeneratedRecipeAdapter : RecyclerView.Adapter<UserGeneratedRecipeAdapter.ViewHolder>() {

    private var userGeneratedRecipes = mutableListOf<UserGeneratedRecipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_generated_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userGeneratedRecipe = userGeneratedRecipes[position]
        holder.bind(userGeneratedRecipe)

        holder.itemView.setOnLongClickListener {
            confirmDelete(holder.itemView.context, position)
            true
        }
    }

    override fun getItemCount(): Int {
        return userGeneratedRecipes.size
    }

    fun setUserGeneratedRecipes(recipes: List<UserGeneratedRecipe>) {
        this.userGeneratedRecipes = recipes.toMutableList()
        notifyDataSetChanged()
    }

    private fun confirmDelete(context: Context, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Yes") { _, _ ->
                deleteRecipe(position)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteRecipe(position: Int) {
        val recipeToDelete = userGeneratedRecipes[position]

        FirebaseDatabase.getInstance().reference
            .child("recipes")
            .child(recipeToDelete.id)
            .removeValue()


        userGeneratedRecipes.removeAt(position)
        notifyItemRemoved(position)
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
