package fi.organization.recipeapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@SuppressLint("Registered")
class RecipeDetailsActivity : AppCompatActivity() {
    lateinit var labelText : TextView
    lateinit var scrollView : ScrollView
    lateinit var ingredientText : TextView
    lateinit var saveButton : Button
    lateinit var sourceButton : Button

    // Recipe info
    var url : String? = ""
    var label : String? = ""
    var ingredients : MutableList<String>? = null
    var source : String? = ""
    var savedRecipes: ArrayList<Recipe> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_details)
        Log.d("RecipeDetailsActivity", "onCreate()")

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)
        ingredientText = scrollView.findViewById(R.id.ingredientText)
        sourceButton = findViewById(R.id.sourceButton)
        saveButton = findViewById(R.id.saveButton)

        // LoadData and see it this recipe is already saved. Call saveData()
        saveButton.setOnClickListener() {
            loadData()
            if (!savedRecipes.any{ recipe -> recipe.label == label}) {
                Toast.makeText(this@RecipeDetailsActivity, "Recipe saved", Toast.LENGTH_SHORT).show()
                saveData()
            } else {
                Toast.makeText(this@RecipeDetailsActivity, "Already saved", Toast.LENGTH_SHORT).show()
            }

        }

        val intent = getIntent()
        val extras = intent.extras
        if (extras != null) {
            label = extras.getString("label")
            val ingredientLines = extras.getString("ingredientLines")
            source = extras.getString("source")
            url = extras.getString("sourceUrl").toString()

            ingredients = ingredientLines?.split("#")?.toMutableList()
            val lineArray = ingredients?.joinToString("\n\n")
            ingredientText.text = lineArray

            labelText.text = label

            sourceButton.text = source
            sourceButton.setOnClickListener() {
                sourceButtonClicked(it)
            }
            Log.d("OK", ingredientLines)

        }
    }

    // Add new recipe to savedRecipes and save savedRecipes
    fun saveData() {

        var myRecipeObject = Recipe(label = label, ingredientLines = ingredients, url = url, source = source)
        savedRecipes.add(myRecipeObject)
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(savedRecipes)
        editor.putString("recipeList", json)
        editor.apply()
    }
    fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("recipeList", ArrayList<String>().toString())
        val gson = Gson()
        val collectionType = object : TypeToken<ArrayList<Recipe>>() {}.type
        savedRecipes = gson.fromJson(json, collectionType)
        Log.d("GOT RECIPES", savedRecipes.toString())
    }

    // Start activity on browser with url
    fun sourceButtonClicked(button: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }


    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }

}