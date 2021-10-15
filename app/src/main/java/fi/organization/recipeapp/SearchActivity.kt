package fi.organization.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.fasterxml.jackson.databind.ObjectMapper


class SearchActivity : AppCompatActivity() {
    lateinit var listView : ListView
    var listItems :  ArrayList<Recipe> = ArrayList()

    lateinit var adapter : ArrayAdapter<String>

    lateinit var searchBar : EditText
    lateinit var searchButton : Button
    lateinit var innerLayout : LinearLayout

    lateinit var buttonPanel : LinearLayout
    lateinit var left : LinearLayout
    lateinit var prevButton : Button
    lateinit var nextButton : Button
    lateinit var toFirstButton : Button

    // How many items are showed to user. Default is 0-10.
    lateinit var howManyItems : TextView
    private var min : Int = 0
    private var max : Int = 10

    private var searchWord : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        listView = findViewById(R.id.listView)

        innerLayout = findViewById(R.id.innerLayout)
        searchBar = innerLayout.findViewById(R.id.searchBar)
        searchButton = innerLayout.findViewById(R.id.searchButton)
        searchButton.setOnClickListener() {
            searchClicked(it)
        }

        buttonPanel = findViewById(R.id.buttonPanel)
        left = buttonPanel.findViewById(R.id.left)
        toFirstButton = left.findViewById(R.id.backToFirst)
        prevButton = left.findViewById(R.id.prevButton)
        nextButton = buttonPanel.findViewById(R.id.nextButton)
        howManyItems = findViewById(R.id.howManyItems)


        // User sees items 0-10
        toFirstButton.setOnClickListener() {
            min = 0
            max = 10
            searchClicked(it)
        }

        // User sees previous 10 items
        prevButton.setOnClickListener() {
            if (min >= 10) {
                min -= 10
                max -= 10
            }
            searchClicked(it)
        }

        // User sees next 10 items
        nextButton.setOnClickListener() {
            if (max < 100) {
                min += 10
                max += 10
            }

            searchClicked(it)
        }

        adapter = ArrayAdapter(this, R.layout.item, R.id.myTextView, ArrayList<String>())
        listView.adapter = adapter

        // Each item on listview has onclicklistener that starts RecipeDetailsActivity
        listView.setOnItemClickListener{parent, view, position, id ->
            Toast.makeText(this@SearchActivity, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show()
            for (recipe in listItems) {
                if (recipe.label.toString() === parent.getItemAtPosition(position).toString()) {
                    val intent = Intent(this, RecipeDetailsActivity::class.java)
                    intent.putExtra("label", recipe.label.toString())
                    intent.putExtra("sourceUrl", recipe.url.toString())
                    intent.putExtra("source", recipe.source.toString())
                    intent.putExtra("ingredientLines", recipe.ingredientLinesToString())
                    startActivity(intent)
                }
            }
        }
    }



    private fun searchClicked(button: View) {
        howManyItems.text = "Showing results: ${min} - ${max}"
        searchWord = searchBar.text.toString()
        searchRecipes()
    }


    private fun searchRecipes() {
        var url = "https://api.edamam.com/search?q=${searchWord}&app_id=${yourAppId}&app_key=${yourAppKey}&&from=${min}&to=${max}&calories=591-722&health=alcohol-free"
        downloadUrlAsync(this, url) { it ->
            // Clear existing lists
            adapter.clear()
            listItems.clear()
            val mp = ObjectMapper()
            val myObject: RecipeJsonObject= mp.readValue(it, RecipeJsonObject::class.java)
            val recipes: MutableList<Hit>? = myObject.hits

            // Add new recipes to list
            recipes?.forEach() {
                adapter.add(it.toString())
                it.recipe?.let { it1 -> listItems.add(it1) }
            }
        }
    }

}