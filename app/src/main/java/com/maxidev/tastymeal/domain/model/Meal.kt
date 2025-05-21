package com.maxidev.tastymeal.domain.model

data class Meal(
    val idMeal: String = "",
    val strMeal: String = "",
    val strMealThumb: String = "",
    val strCategory: String = "",
    val strInstructions: String = "",
    val strTags: String = "",
    val strYouTube: String = "",
    val strSource: String = "",
    val strIngredients: List<String> = emptyList(),
    val strMeasure: List<String> = emptyList(),
    val bookmarked: Boolean = false
)