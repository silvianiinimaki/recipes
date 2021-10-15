package fi.organization.recipeapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SavedRecipeDetailsActivity : AppCompatActivity() {
    private var image: String? = ""
    lateinit var labelText : TextView
    lateinit var scrollView : ScrollView
    lateinit var linearLayout: LinearLayout
    lateinit var ingredientText : TextView
    lateinit var directionText : TextView
    lateinit var imageView : ImageView
    lateinit var deleteButton : Button
    lateinit var editButton : Button
    lateinit var sourceButton : Button
    lateinit var ingredientScroll : ScrollView
    lateinit var directionScroll : ScrollView

    // recipe details
    var url : String? = ""
    var label : String? = ""
    var ingredients : MutableList<String>? = null
    var directions : MutableList<String>? = null
    var source : String? = ""
    var savedRecipes: ArrayList<Recipe> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_recipe_details)

        loadData()

        labelText = findViewById(R.id.labelText)
        scrollView = findViewById(R.id.scrollView)

        linearLayout = scrollView.findViewById(R.id.linearLayout)
        ingredientScroll = linearLayout.findViewById(R.id.ingredientScroll)
        ingredientText = ingredientScroll.findViewById(R.id.ingredientText)
        directionScroll = linearLayout.findViewById(R.id.directionScroll)
        directionText = directionScroll.findViewById(R.id.directionText)
        imageView = linearLayout.findViewById(R.id.imageView)
        sourceButton = findViewById(R.id.sourceButton)
        deleteButton = findViewById(R.id.deleteButton)
        editButton = findViewById(R.id.editButton)

        // Check extras
        val intent = getIntent()
        val extras = intent.extras
        if (extras != null) {
            label = extras.getString("label")
            labelText.text = label

            val ingredientLines = extras.getString("ingredientLines")
            val directionLines = extras.getString("directionLines")
            ingredients = ingredientLines?.split("#")?.toMutableList()
            val ingredientLineString = ingredients?.joinToString("\n\n")
            directions = directionLines?.split("#")?.toMutableList()
            val directionLineString = directions?.joinToString ( "\n\n" )
            ingredientText.text = ingredientLineString
            directionText.text = directionLineString

            source = extras.getString("source")
            if (source?.length!! > 0) {
                sourceButton.text = source
            }

            url = extras.getString("sourceUrl").toString()
            image = extras.getString("image").toString()

            val takenImage = BitmapFactory.decodeFile(image)
            if (takenImage != null) {
                Log.d("PATH", takenImage.toString())
                imageView.setImageBitmap(takenImage as Bitmap?)
            }

            sourceButton.setOnClickListener() {
                sourceButtonClicked(it)
            }
            deleteButton.setOnClickListener() {
                deleteButtonClicked(it)
            }
            editButton.setOnClickListener() {
                editButtonClicked(it)
            }

        }

    }

    // Open browser with url
    fun sourceButtonClicked(button: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    // Delete recipe with label from savedRecipes and save new array
    fun deleteButtonClicked(button: View) {
        val newSavedRecipes : ArrayList<Recipe> = ArrayList()
        savedRecipes.filterTo(newSavedRecipes, {it.label != label})
        savedRecipes = newSavedRecipes
        saveData()
        this.finish()
    }

    // Start newRecipeActivity and send details to it
    fun editButtonClicked(button: View) {
        val intent = Intent(this, NewRecipeActivity::class.java)
        var recipe = Recipe(label = label, ingredientLines = ingredients, url = url, source = source, directionLines = directions, image = image)
        intent.putExtra("label", recipe.label.toString())
        intent.putExtra("sourceUrl", recipe.url.toString())
        intent.putExtra("source", recipe.source.toString())
        intent.putExtra("ingredientLines", recipe.ingredientLinesToString())
        intent.putExtra("directionLines", recipe.directionLinesToString())
        intent.putExtra("image", recipe.image.toString())
        startActivity(intent)
    }

    fun saveData() {
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

    // When user edits recipe in newRecipeActivity and goes back, this activity won't refresh
    // because MyRecipes sends the info used here with intent. The info will update if we go back to MyRecipes first.
    override fun onRestart() {
        super.onRestart()
        this.finish()
    }
}