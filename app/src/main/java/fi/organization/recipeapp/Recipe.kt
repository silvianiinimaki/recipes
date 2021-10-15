package fi.organization.recipeapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Recipe(var uri: String? = null, var label: String? = null, var image: String? = null, var source: String? = null, var url: String? = null, var ingredientLines: MutableList<String>? = null, var directionLines: MutableList<String>? = null) {

    override fun toString() : String{
        return "$label"
    }
    fun ingredientLinesToString() : String {
        var str = ""
        if (ingredientLines != null) {
            for(line in this!!.ingredientLines!!) {
                str += "$line#"
            }
        }
        return str
    }
    fun directionLinesToString() : String {
        var str = ""
        if (directionLines != null) {
            for(line in this?.directionLines!!) {
                str += "$line#"
            }
        }
        return str
    }
}