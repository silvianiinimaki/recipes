package fi.organization.recipeapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

private const val REQUEST_CODE = 42

class NewRecipeActivity : AppCompatActivity() {

    // Recipe details
    private var FILE_NAME : String = ""
    private lateinit var photoFile: File
    private var originalLabel : String? = ""
    private var label : String? = ""
    private var source : String? = ""
    private var url : String = ""
    private var image: String? = ""
    private var ingredients : MutableList<String>? = arrayListOf()
    private var directions : MutableList<String>? = arrayListOf()
    private var savedRecipes: ArrayList<Recipe> = ArrayList()

    lateinit var scrollView: ScrollView
    lateinit var linearLayout: LinearLayout
    lateinit var editLabel: EditText

    // Add ingredient
    lateinit var ingredienText: TextView
    lateinit var ingredientsLayout: LinearLayout
    lateinit var editIngredients: EditText
    lateinit var addIngredientButton: Button
    lateinit var removeIngredientButton: Button

    // Add direction
    lateinit var directionText: TextView
    lateinit var directionsLayout: LinearLayout
    lateinit var editDirections: EditText
    lateinit var addDirectionButton: Button
    lateinit var removeDirectionButton: Button

    // Add other details
    lateinit var editSource: EditText
    lateinit var editUrl: EditText
    lateinit var cameraButton: Button
    lateinit var imageView: ImageView

    // Cancel or save
    lateinit var buttonPanel: LinearLayout
    lateinit var cancelButton: Button
    lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_recipe)
        loadData()

        scrollView = findViewById(R.id.scrollView)
        linearLayout = scrollView.findViewById(R.id.linearLayout)
        editLabel = linearLayout.findViewById(R.id.editLabel)
        cameraButton = scrollView.findViewById(R.id.cameraButton)
        imageView = scrollView.findViewById(R.id.imageView)

        // Start camera-activity for result
        cameraButton.setOnClickListener() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            FILE_NAME = "$label.jpg"

            photoFile = getPhotoFile(FILE_NAME)
            // Wrap the file object as a contentProvider using FileProvider-class
            val fileProvider = FileProvider.getUriForFile(this, "fi.organization.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            // Take picture if the device has a camera and FILE_NAME is not empty
            if (takePictureIntent.resolveActivity(this.packageManager) != null && FILE_NAME.isNotEmpty()) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
        // Add ingredients
        ingredienText = linearLayout.findViewById(R.id.ingredientText)
        ingredientsLayout = linearLayout.findViewById(R.id.ingredientsLayout)
        editIngredients = ingredientsLayout.findViewById(R.id.editIngredients)
        addIngredientButton = ingredientsLayout.findViewById(R.id.addIngredientButton)
        addIngredientButton.setOnClickListener() {
            addIngredient()
        }
        // Remove ingredients
        removeIngredientButton = ingredientsLayout.findViewById(R.id.removeIngredientButton)
        removeIngredientButton.setOnClickListener() {
            if (ingredients?.size!! > 0) {
                // Remove latest ingredient
                ingredients?.size?.minus(1)?.let { it1 -> ingredients?.removeAt(it1) }
                var ingredientsLinesToString = ingredients?.joinToString("\n")
                ingredienText.text = ingredientsLinesToString
            }
        }


        directionText = linearLayout.findViewById(R.id.directionText)
        directionsLayout = linearLayout.findViewById(R.id.directionsLayout)
        editDirections = directionsLayout.findViewById(R.id.editDirections)
        // Add direction
        addDirectionButton = directionsLayout.findViewById(R.id.addDirectionButton)
        addDirectionButton.setOnClickListener() {
            addDirection()
        }
        // Remove direction
        removeDirectionButton = directionsLayout.findViewById(R.id.removeDirectionButton)
        removeDirectionButton.setOnClickListener() {
            if (directions?.size!! > 0) {
                directions?.size?.minus(1)?.let { it1 -> directions?.removeAt(it1) }
                var directionsLinesToString = directions?.joinToString("\n")
                directionText.text = directionsLinesToString
            }


        }

        // Add other details
        editSource = linearLayout.findViewById(R.id.editSource)
        editUrl = linearLayout.findViewById(R.id.editUrl)
        buttonPanel = findViewById(R.id.buttonPanel)

        // Cancel
        cancelButton = buttonPanel.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener() {
            this.finish()
        }

        // Save
        saveButton = buttonPanel.findViewById(R.id.saveButton)
        saveButton.setOnClickListener() {
            saveRecipe()
        }

        // Get extras
        val intent = getIntent()
        val extras = intent.extras
        if (extras != null) {
            label = extras.getString("label")
            originalLabel = label
            editLabel.setText(label)

            val ingredientLines = extras.getString("ingredientLines")
            val directionLines = extras.getString("directionLines")

            ingredients = ingredientLines?.split("#")?.toMutableList()
            ingredients = ingredients?.filter{ it.isNotEmpty() }?.toMutableList()
            val ingredientLineString = ingredients?.joinToString("\n")
            ingredienText.text = ingredientLineString

            directions = directionLines?.split("#")?.toMutableList()
            directions = directions?.filter{ it.isNotEmpty() }?.toMutableList()
            val directionLineString = directions?.joinToString ( "\n" )
            directionText.text = directionLineString

            source = extras.getString("source")
            editSource.setText(source)

            image = extras.getString("image").toString()
            val takenImage = BitmapFactory.decodeFile(image)
            imageView.setImageBitmap(takenImage as Bitmap?)

            url = extras.getString("sourceUrl").toString()
            editUrl.setText(url)
            
        }
    }

    // Add one direction line to directions -mutablelist
    fun addDirection() {
        val str = editDirections.text.toString()
        if (str.length > 0 && str !== "\n") {
            directions?.add(str)
            val directionsLinesToString = directions?.joinToString("\n")
            directionText.text = directionsLinesToString
        }
        editDirections.getText().clear()
    }

    // Add one ingredient line to ingredients -mutablelist
    fun addIngredient() {
        val str = editIngredients.text.toString()
        if (str.length > 0 && str !== "\n") {
            ingredients?.add(str)
            val ingredientsLinesToString = ingredients?.joinToString("\n")
            ingredienText.text = ingredientsLinesToString
        }
        editIngredients.getText().clear()
    }

    // Check if label is unique, call saveData and finish this activity
    fun saveRecipe() {
        loadData()
        label = editLabel.text.toString()
        url = editUrl.text.toString()
        source = editSource.text.toString()
        if (!savedRecipes.any{ recipe -> recipe.label == label}) {
            Toast.makeText(this@NewRecipeActivity, "Recipe saved", Toast.LENGTH_SHORT).show()
            saveData()
            this.finish()
            // Check if user is editing old recipe. Delete old and save new
        } else if (savedRecipes.any{ recipe -> recipe.label == originalLabel}) {
            val newSavedRecipes : ArrayList<Recipe> = ArrayList()
            savedRecipes.filterTo(newSavedRecipes, {it.label != originalLabel})
            savedRecipes = newSavedRecipes
            saveData()
            Toast.makeText(this@NewRecipeActivity, "Recipe saved", Toast.LENGTH_SHORT).show()
            this.finish()
        } else {
            Toast.makeText(this@NewRecipeActivity, "You already have a recipe with this label", Toast.LENGTH_SHORT).show()
        }
    }

    // Filter ingredients and directions from empty items
    // Add the new recipeObject to savedRecipes
    // Save savedRecipes to sharedPreferences
    fun saveData() {
        if (label?.isNotEmpty()!!) {
            ingredients = ingredients?.filter{ it.isNotEmpty() }?.toMutableList()
            directions = directions?.filter{ it.isNotEmpty() }?.toMutableList()
            Log.d("SAVING", directions.toString())
            var myRecipeObject = Recipe(label = label, ingredientLines = ingredients, url = url, source = source, directionLines = directions, image = image)
            savedRecipes.add(myRecipeObject)
            val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(savedRecipes)
            editor.putString("recipeList", json)
            editor.apply()
        }
    }


    fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("recipeList", ArrayList<String>().toString())
        val gson = Gson()
        val collectionType = object : TypeToken<ArrayList<Recipe>>() {}.type
        savedRecipes = gson.fromJson(json, collectionType)
        Log.d("GOT RECIPES", savedRecipes.toString())
    }


    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            image = photoFile.absolutePath
            imageView.setImageBitmap(takenImage as Bitmap?)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}