package fi.organization.recipeapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeJsonObject(var hits: MutableList<Hit>? = null)
