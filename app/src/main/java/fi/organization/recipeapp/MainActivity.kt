package fi.organization.recipeapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var search : Button
    lateinit var newRecipe : Button
    lateinit var myRecipes : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search = findViewById(R.id.searchActivityButton)
        search.setOnClickListener() {
            changeActivityToSearch(it)
        }
        newRecipe = findViewById(R.id.createRecipeButton)
        newRecipe.setOnClickListener() {
            changeActivityToNewRecipe(it)
        }
        myRecipes = findViewById(R.id.myRecipesButton)
        myRecipes.setOnClickListener() {
            changeActivityToMyRecipes(it)
        }

    }
    private fun changeActivityToSearch(button: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
    private fun changeActivityToNewRecipe(button: View) {
        val intent = Intent(this, NewRecipeActivity::class.java)
        startActivity(intent)
    }
    private fun changeActivityToMyRecipes(button: View) {
        val intent = Intent(this, MyRecipes::class.java)
        startActivity((intent))
    }

}
