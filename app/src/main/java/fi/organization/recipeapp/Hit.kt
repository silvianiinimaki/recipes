package fi.organization.recipeapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.lang.Math.round
import java.math.RoundingMode
import java.text.DecimalFormat

@JsonIgnoreProperties(ignoreUnknown = true)
data class Hit(var recipe: Recipe? = null) {
    fun getImage() : String? {
        return recipe?.image
    }
    override fun toString() : String{
        return recipe.toString()
    }

}